
- “Storage, Memory and the Stack”
  - `storage` : Corresponding to the storage in MPT, like the hard drive on PC.
    - State variables and Local Variables of `structs`, `array` are always stored in storage by default.
  - `memory`: Contract obtains a freshly cleared instance for each message call, like the memory on PC
    - Function arguments are in memory.
    - Whenever a new instance of an array is created using the keyword ‘memory’, a new copy of that variable is created. Changing the array value of the new instance does not affect the original array.



- Functions/Variables Visibility 
  - `external` − External functions are meant to be called by other contracts. They cannot be used for internal call. To call external function within contract this.function_name() call is required. State variables cannot be marked as external.
  - `public` − Public functions/ Variables can be used both externally and internally. For public state variable, Solidity automatically creates a getter function.
  - `internal` − Internal functions/ Variables can only be used internally or by derived contracts.
  - `private` − Private functions/ Variables can only be used internally and not even by derived contracts.
