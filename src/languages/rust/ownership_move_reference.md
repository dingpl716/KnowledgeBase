## Ownership, Move

- 把一个变量赋值给另一个变量，实际上是让渡了所有权，原来变量的内存空间变为 uninitiated
```rust
let data: Vec<u32> = vec![1, 2, 3, 4];
let data1: Vec<u32> = data; // 创建一个新的指针data1，并且让它指向data所指向的内存。
```
  在这个例子中，`data`是一个指针，指向堆上的vector. 当`data`被赋给`data1`后，所有权也进行了让渡，此时再访问`data`就会出错。

- 调用`our_print`函数会让渡所有权，因为它要求的入参是Vec<T>
```rust
fn our_print<T>(Vec<T>) {
    println!()
}
``` 

- 不能把一个数组中的其中一个元素的所有权给弄出来，因为这样就相当于要求 Rust 去追踪哪一个元素是可用的，哪一个元素的内存已经 uninitiated 了，这显然太负责了。如果你非得有这个需求的话，你有三种 work around
    ```rust
    // Build a vector of the strings "101", "102", ... "105"
    let mut v = Vec::new();
    for i in 101 .. 106 {
        v.push(i.to_string());
    }

    // Pull out random elements from the vector.
    let third = v[2]; // error: Cannot move out of index of Vec
    let fifth = v[4]; // here too

    // 1. Pop a value off the end of the vector:
    let fifth = v.pop().expect("vector empty!");
    assert_eq!(fifth, "105");

    // 2. Move a value out of a given index in the vector,
    // and move the last element into its spot:
    let second = v.swap_remove(1);
    assert_eq!(second, "102");

    // 3. Swap in another value for the one we're taking out:
    let third = std::mem::replace(&mut v[2], "substitute".to_string());
    assert_eq!(third, "103");

    // Let's see what's left of our vector.
    assert_eq!(v, vec!["101", "104", "substitute"]);
    ```

## Reference, Borrow

- **除了像i32，char，f64，bool这种类型外或者这些类型的tuple外，其他type都是指针，String 是指针，Vec<T> 是指针，Struct 是指针，&str 是指针，&[T] 是指针，&Struct 也是指针。但是前面三个是 owning pointers，后面三个是没有所有权的指针，这些没有所有权的指针叫 reference。**
- 所以一个函数接收的是类型是 Struct，我们会说这个函数叫 pass by value，但这不是说把 heap 的数据给传进去了，而只是因为我们把 value 的所有权传进去了才这么叫它
- The `&` mark "borrows" a reference pointing to a piece of data. It actually create a new pointer pointing to the data.
- `.` 操作符很强大，它可以帮你隐式的干两件事
  - 自动做 dereference，相当于自动帮你加上 `*` 操作符
    ```rust
    struct Anime { name: &'static str, bechdel_pass: bool };
    let aria = Anime { name: "Aria: The Animation", bechdel_pass: true };
    let anime_ref = &aria;
    assert_eq!(anime_ref.name, "Aria: The Animation");

    // Equivalent to the above, but with the dereference written out:
    assert_eq!((*anime_ref).name, "Aria: The Animation");
    ```
  - 自动帮你 borrow reference，相当于自动加上 `&` 操作符
    ```rust
    let mut v = vec![1973, 1968];
    v.sort();           // implicitly borrows a mutable reference to v
    (&mut v).sort();    // equivalent, but more verbose
    ```

### Reference, Borrow原则
- 情况一：在同一时间内，一个数据可以有**多个只读的引用**，引用的作用范围可以有交叉
- 情况二：在同一作用域内，如果有可变的引用，那么首先不能有其他**只读**的引用了，并且多个可变引用的**活跃时间不能有交叉**。
- 当有任何一种引用存活的时候，就连 owning pointer 也不能修改那个值了。

#### Example 1
- 编译错误, error[E0502]: cannot borrow `data` as immutable because it is also borrowed as mutable
- 因为在第一次可变引用的作用范围内，你不能再借第二次
```rust
fn main() {
    let mut data: Vec<u32> = vec![1, 2, 3, 4];
    let data1: &mut Vec<u32> = &mut data; // 第一个引用，可变
    
    data1.push(5); // 第一个引用，可变
    println!("{:?}", data); // 第二个引用，不可变
    
    data1.push(6); // 第一个引用，可变
}
```

#### Example 2
- 编译错误, cannot borrow `data` as mutable more than once at a time
- 因为第一次和第二次有交叉
```rust
fn main() {
    let mut data: Vec<u32> = vec![1, 2, 3, 4];
    let data1: &mut Vec<u32> = &mut data; // 第一个引用，可变
    
    data1.push(5); // 第一个引用，可变
    println!("{:?}", data1); // 第一个引用，可变
    
    data.push(6); // 第二个引用，可变
    println!("{:?}", data1);  // 第一个引用，可变
}
```

#### Example 3
- 无编译错误
- 第一次输出`[1, 2, 3, 4, 5]`
- 第二次输出`[1, 2, 3, 4, 5, 6]`
```rust
    let mut data: Vec<u32> = vec![1, 2, 3, 4];
    let data1: &mut Vec<u32> = &mut data; // 第一个引用，可变
    
    data1.push(5); // 第一个引用，可变
    println!("{:?}", data1); // 第一个引用，可变
    
    data.push(6); // 第二个引用，可变
    println!("{:?}", data);  // 第二个引用，可变
```

#### Example 4
- 无编译错误
- 一次输出`[1, 2, 3, 4, 5]`
- 第二次输出`[1, 2, 3, 4, 5, 6]`
- 第三次输出`[1, 2, 3, 4, 5, 6， 7]`
```rust
    let mut data: Vec<u32> = vec![1, 2, 3, 4];
    let data1: &mut Vec<u32> = &mut data; // 第一个引用，可变

    data1.push(5); // 第一个引用，可变
    println!("{:?}", data1); // 第一个引用，可变

    data.push(6); // 第二个引用，可变
    println!("{:?}", data); // 第二个引用，可变

    data.push(7); // 第二个引用，可变
    println!("{:?}", data); // 第二个引用，可变
```

#### Example 5
- 编译错误，error[E0499]: cannot borrow `data` as mutable more than once at a time
- 第一次第二次有交叉
```rust
    let mut data: Vec<u32> = vec![1, 2, 3, 4];
    let data1: &mut Vec<u32> = &mut data; // 第一个引用，可变

    data1.push(5); // 第一个引用，可变
    println!("{:?}", data1); // 第一个引用，可变

    data.push(6); // 第二个引用，可变
    println!("{:?}", data); // 第二个引用，可变

    data1.push(7); // 第一个引用，可变
```
