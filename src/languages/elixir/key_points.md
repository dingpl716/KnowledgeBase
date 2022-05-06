## Key points

- `.exs` file will not be compiled to disk, instead the compiled file will only be stored in memory.
- Should use as less `case` as possible, we should push down such logic to function clause pattern match. In this way we can keep the "main" function focusing on the bare-bones.
- Should use as many `Stream` as possible and as less `Enum` as possible. The `Enum` iterates the data too much, but `Stream` will do it in one pass.
- The whole BEAM instance just use a single OS process, but it will use as many OS thread as the number of logical CPU cores. In each thread there is a scheduler which is in charge of multiple VM process.
- If a VM process calls `Process.sleep` or `receive`, it yields execution back to scheduler so that other process can start to execute.
- IO is actually done in another async thread. If a VM process issues `IO` calls, the calling process is preempted, and other process get execution slot. After the IO is done, the scheduler resumes the calling process. This makes the IO looks synchronous but it is reality asynchronous.
- By default, BEAM fires up 10 async thread, but this can be changed by `+A n` Erlang flag.
- Each VM process can only execute one thing at a time, so make sure you understand in which cases is a process blocked or preempted:
  - `receive`
  - IO
  - `GenServer.start_link/init`
    - Long running`GenServer.start_link` will block the caller process.
    - To by pass this, you can let GenServer send a message in `init` method to the server process, in this way the `start_link` will return immediately, and we can init the state in `init` method
- Three types of exception
  - `:error`, if it is not handled in a process, that process would be terminated.
  - `:exit`, is a message sent to a process
  - `:throw`, this is used for non local return. Since there is no `return` and `break` keywords, so you can use `throw` to return from a nested recursion call. This is sort of `goto` which you should avoid to use. 
- Via tuples, 
  - `{:via, some_module, {arg1, arg2}}`, this form equals:
    - `some_module.register(arg1, arg2)`
    - `some_module.lookup(arg1, arg2)`
  - `{:via, some_module, {arg1, arg2, arg3}}`, this form equals:
    - `some_module.register(arg1, arg2, arg3)`
    - `some_module.lookup(arg1, arg2)`
- `child_spec` is a map having 6 keys:
  - `id`: must have, cannot be duplicated globally
  - `modules`
  - `type`, either `:supervisor` or `:worker`, denoting the type of this process.
  - `start`, must have, it's a `{module, function, [args]}` tuple, supervisor will call `module.function(args)` to restart the process.
  - `restart`, under what condition to restart a process
    - `:permanent`, always restart a process when it crashes
    - `:temporary`, don't restart
    - `:transient`, restart only if it terminates abnormally (doesn't exit with `:normal` reason)
```elixir
%{
    id: 
    modules: 
    type: :supervisor | :worker,
    start: {module, function, [args]},
    restart: :temporary | :transient
    shutdown: 
}
```
