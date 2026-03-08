- Debug trait -> "{:?}"
- Clone trait -> 让数据结构可以被复制
- Copy trait
- Static variable

## Key Points
- For arithmetic operation overflows, rust panic in debug build, but wraps around in release build, it produces the value equivalent to the mathematically correct result modulo the range of the value
- checked_add/mul/, wrapping_add/mul
- *byte literals*: `b'A'` is equivalent to `65u8`
- `char`: 32bit long
- The expression `&x` produces a reference to `x`; in Rust terminology, we say that it **borrows** a reference to x.

## Atomic Types
[Official Document](https://doc.rust-lang.org/std/sync/atomic/index.html)

## Closure

- `move`关键字是指当closure捕获一个参数的时候，到底是捕获对它的引用还是捕获所有权
- 如果move了被捕获参数的所有权，那么闭包就不能call两次，比如下面的例子
```rust
let name = String::from("Tyr");

// 这个闭包会 clone 内部的数据返回，所以它不是 FnOnce
let c = move |greeting: String| (greeting, name);

println!("c call once: {:?}", c("qiao".into()));

// 这次调用会报错
println!("c call twice: {:?}", c("bonjour".into()));
```

- 但是如果我们捕获的不是参数本身，而只捕获它的一个clone，那就可以call多次

```rust
let name = String::from("Tyr");

// 这个闭包会 clone 内部的数据返回，所以它不是 FnOnce
let c = move |greeting: String| (greeting, name.clone());

// 所以 c 可以被调用多次
println!("c call once: {:?}", c("qiao".into()));
println!("c call twice: {:?}", c("bonjour".into()));
```

- Function that accepts a closure
```rust
fn map<B, F>(self, f: F) -> Map<Self, F>
where
    Self: Sized,
    F: FnMut(Self::Item) -> B,
{
    Map::new(self, f)
}
```

- Function that returns a closure
```rust
use std::ops::Mul;

fn main() {
    let c1 = curry(5);
    println!("5 multiply 2 is: {}", c1(2));

    let adder2 = curry(3.14);
    println!("pi multiply 4^2 is: {}", adder2(4. * 4.));
}

fn curry<T>(x: T) -> impl Fn(T) -> T
where
    T: Mul<Output = T> + Copy,
{
    move |y| x * y
}
```



## Questions:
- why sometimes we have to do `self.0 = img`, but sometimes we don't
- When match a Enum, what's the difference between Self and &Self
  ```rust
  match self {
            &Self::InitializeMint {
                ref mint_authority: &Pubkey,
                ref freeze_authority: &COption<Pubkey>,
                decimals: u8,
            } => {
                buf.push(0);
                buf.push(decimals);
                buf.extend_from_slice(mint_authority.as_ref());
                Self::pack_pubkey_option(freeze_authority, &mut buf);
            }
            &Self::InitializeAccount2 { owner: Pubkey } => {
                buf.push(16);
                buf.extend_from_slice(owner.as_ref());
            }
            Self::SetAuthority {
                authority_type: &AuthorityType,
                ref new_authority &COption<Pubkey>,
            } => {
                buf.push(6);
                buf.push(authority_type.into());
                Self::pack_pubkey_option(new_authority, &mut buf);
            }
            Self::InitializeAccount => buf.push(1),

  }
  ```
