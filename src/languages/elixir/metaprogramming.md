## Metaprogramming in Elixir

- A macro receives an AST, manipulate it and returns a new AST, it's the code that writes other code.
- The `defmodule` is also a macro, so you can think of the whole elixir source file as calling of macros
```elixir
defmodule MyModule do
end
```
- When the compiler encounters this source file, it first gets the AST returned by the `defmoudle` macro, and then expand AST and compile to bytecode.
- `quote` convert source code to AST
- `unquote` injects an AST into another AST

### Generate least code, import least code
- In following example, when the caller module `import Assertion`, it will only import the `assert` macro, and will not import other `assert` functions defined in `Assertion.Test` module.
- We didn't put the whole set of `assert` functions into the quote, but we only put a function that calls this set of function. In this way, the caller module will only have a calling function generated in its context instead of the whole implementation.
```elixir
defmodule Assertion do
    defmacro assert({operator, _, [lhs, rhs]}) do
        quote bind_quoted: [operator: operator, lhs: lhs, rhs: rhs] do
            Assertion.Test.assert(operator, lhs, rhs)                     
        end
    end
end

defmodule Assertion.Test do                                         
    def assert(:==, lhs, rhs) when lhs == rhs do
        IO.write "."
    end
    def assert(:==, lhs, rhs) do
        IO.puts """
        FAILURE:
        Expected:       #{lhs}
        to be equal to: #{rhs}
        """
    end

    def assert(:>, lhs, rhs) when lhs > rhs do
        IO.write "."
    end
    def assert(:>, lhs, rhs) do
        IO.puts """
        FAILURE:
        Expected:           #{lhs}
        to be greater than: #{rhs}
        """
    end
end
```

### Caller context, macro context

```elixir
defmodule Mod do
  defmacro definfo do
    IO.puts "In macro's context (#{__MODULE__})." 
    IO.puts "In macro's context #{unquote(__MODULE__)}"

    quote do
      IO.puts "In caller's context (#{__MODULE__})."
      IO.puts "In caller's context #{unquote(__MODULE__)}"

      def friendly_info do
        IO.puts """
        My name is #{__MODULE__}
        My functions are #{inspect __info__(:functions)}
        """
      end
    end
  end
end

defmodule MyModule do
  require Mod
  Mod.definfo
end
```

When you compile the code above, you will get following output
```
In macro's context (Elixir.Mod).
In macro's context Elixir.Mod
In caller's context (Elixir.MyModule).
In caller's context Elixir.Mod
```

### @before_compile

- This attribute accepts a module as the argument, and the module must define `_before_compile_/1` macro
- The name of this attribute is confusing, it actually means "before compilation is done. When compiler sees this attribute, it will call the module's `_before_compile/1` macro to expand caller module's code before it finishes compiling the caller module.


### unquote_fragments

```elixir
defmodule Stela do
  # {
  #   "inputs": [
  #     {
  #       "internalType": "address",
  #       "name": "to",
  #       "type": "address"
  #     },
  #     {
  #       "internalType": "uint256",
  #       "name": "tokenId",
  #       "type": "uint256"
  #     }
  #   ],
  #   "name": "approve",
  #   "outputs": [],
  #   "stateMutability": "nonpayable",
  #   "type": "function"
  # }
  abi_list =
    "artifacts/contracts/Stela.sol/Stela.json"
    |> File.read!()
    |> Poison.decode!()
    |> Map.get("abi")

  [constructor] = Enum.filter(abi_list, fn abi -> abi["type"] == "constructor" end)

  args =
    constructor["inputs"]
    |> Enum.map(fn input ->
      input["name"] |> Macro.underscore() |> String.to_atom() |> Macro.var(nil)
    end)

  def deploy(unquote_splicing(args)) do
  end

  functions = Enum.filter(abi_list, fn abi -> abi["type"] == "function" end)

  Enum.map(functions, fn function ->
    func_name = function["name"] |> Macro.underscore() |> String.to_atom()

    parameters =
      Enum.map(function["inputs"], fn input ->
        input["name"] |> Macro.underscore() |> String.to_atom() |> Macro.var(nil)
      end)

    # Dynamically define functions on the fly.
    def unquote(func_name)(unquote_splicing(parameters)) do
    end
  end)
end
```
