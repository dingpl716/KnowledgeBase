
### “Storage, Memory and the Stack”

- `storage` : Corresponding to the storage in MPT, like the hard drive on PC.
  - State variables and Local Variables of `structs`, `array` are always stored in storage by default.
- `memory`: Contract obtains a freshly cleared instance for each message call, like the memory on PC
  - Function arguments are in memory.
  - Whenever a new instance of an array is created using the keyword ‘memory’, a new copy of that variable is created. Changing the array value of the new instance does not affect the original array.
- `calldata`


### Functions/Variables Visibility

- `external` − External functions are meant to be called by other contracts. They cannot be used for internal call. To call external function within contract this.function_name() call is required. State variables cannot be marked as external.
- `public` − Public functions/ Variables can be used both externally and internally. For public state variable, Solidity automatically creates a getter function.
- `internal` − Internal functions/ Variables can only be used internally or by derived contracts.
- `private` − Private functions/ Variables can only be used internally and not even by derived contracts.
- `view` keyword means that function will not modify state of contract, i.e. not modify variables, not emit events etc.
- `pure` means functions do not read or modify the state.

### Receive/Fallback Function 

- A contract can have at most one receive function
- `receive() external payable { ... }` (without the `function` keyword)
- This function cannot have arguments, cannot return anything and must have external visibility and payable state mutability.
- It can be virtual, can override and can have modifiers.
- It is called every time via `.send()` or `.transfer()`

- A contract can have at most one fallback function
- `fallback () external [payable]` or 
- `fallback (bytes calldata input) external [payable] returns (bytes memory output)`
- This function must have `external` visibility.
- A fallback function can be virtual, can override and can have modifiers.

- If neither a receive Ether nor a payable fallback function is present, the contract cannot receive Ether through regular transactions and throws an exception.

### Send/Transfer/Call
- `send`
  - This is the initial way to send ether to an address.
  - If the execution fails, the current contract will not stop with an exception, but return `false`.
  - For the sake of security, always check the return value of `send`
  - Not safe against reentracy.
  - Should avoid use this method.
- `transfer`
  - fails if balance is not large enough, or if the transfer is rejected by receiver
  - throws exception and reverts on failure
  - Since this method only forward 2300 gas stipend, so it is safe against reentrancy
  - Since gas cost could change, so this is not a good way to guard against reentrancy.
  - Should avoid use this method.
- `call{value: amount}`
  - This is the currently recommended way.
  - It is **NOT** safe against reentracy, so you need to use Reentracy Guard or Checks-Effects-Interactions Pattern.

### Require
