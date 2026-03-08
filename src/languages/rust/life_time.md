## Lifetime

Principles:
- 所有引用类型的参数都有独立的生命周期 'a 、'b 等。
- 如果只有一个引用型输入，它的生命周期会赋给所有输出。
- 如果有多个引用类型的参数，其中一个是 self，那么它的生命周期会赋给所有输出。

### Example 1
```rust
// 该函数将会按delimiter将输入的字符串分开，返回前面一段的同时，让原指针指向后面的一段。
pub fn strtok<'a, 'b>(s: &'b mut &'a str, delimiter: char) -> &'a str {
    if let Some(i) = s.find(delimiter) {
        let prefix = &s[..i];
        // 由于 delimiter 可以是 utf8，所以我们需要获得其 utf8 长度，
        // 直接使用 len 返回的是字节长度，会有问题
        let suffix = &s[(i + delimiter.len_utf8())..];
        *s = suffix;
        prefix
    } else {
        // 如果没找到，返回整个字符串，把原字符串指针 s 指向空串
        let prefix = *s;
        *s = "";
        prefix
    }
}

// 生命周期只标注在了&str上， 这种标注的效果和上面的strtok一样
pub fn strtok2<'a>(s: &mut &'a str, delimiter: char) -> &'a str {
    if let Some(i) = s.find(delimiter) {
        let prefix = &s[..i];
        // 由于 delimiter 可以是 utf8，所以我们需要获得其 utf8 长度，
        // 直接使用 len 返回的是字节长度，会有问题
        let suffix = &s[(i + delimiter.len_utf8())..];
        *s = suffix;
        prefix
    } else {
        // 如果没找到，返回整个字符串，把原字符串指针 s 指向空串
        let prefix = *s;
        *s = "";
        prefix
    }
}

pub fn strtok3<'a>(s: &'a mut &str, delimiter: char) -> &'a str {
    if let Some(i) = s.find(delimiter) {
        let prefix = &s[..i];
        // 由于 delimiter 可以是 utf8，所以我们需要获得其 utf8 长度，
        // 直接使用 len 返回的是字节长度，会有问题
        let suffix = &s[(i + delimiter.len_utf8())..];
        *s = suffix;
        prefix
    } else {
        // 如果没找到，返回整个字符串，把原字符串指针 s 指向空串
        let prefix = *s;
        *s = "";
        prefix
    }
}

#[cfg(test)]
mod tests {
    use super::*;
    #[test]
    fn test_strtok() {
        let s = "hello world".to_owned();
        let mut s1 = s.as_str();
        let hello = strtok(&mut s1, ' ');
        println!("hello is: {}, s1: {}, s: {}", hello, s1, s);
        assert_eq!(hello, "hello");
        assert_eq!(s1, "world");
        assert_eq!(s, "hello world");
    }

    #[test]
    fn test_strtok2() {
        let s = "hello world".to_owned();
        let mut s1 = s.as_str();
        let hello = strtok2(&mut s1, ' ');
        println!("hello is: {}, s1: {}, s: {}", hello, s1, s);
        assert_eq!(hello, "hello");
        assert_eq!(s1, "world");
        assert_eq!(s, "hello world");
    }

    // 该test有编译错误
    #[test]
    fn test_strtok_3_1() {
        let s = "hello world".to_owned();
        let mut s1 = s.as_str();
        let hello = strtok3(&mut s1, ' '); //对s1的第一次引用，可变

        // 因为在strok3中，入参和返回值被标注成同一个lifetime，所以下面的hello仍然被认为是对s1的第一次可变引用
        println!("hello is: {}, s: {}", hello, s); //hello相当于是对s1的第一次引用，可变
        println!("s1: {}", s1,); //第二次引用，不可变
    }

    // 该test有编译错误
    #[test]
    fn test_strtok_3_2() {
        let s = "hello world".to_owned();
        let mut s1 = s.as_str();
        let hello = strtok3(&mut s1, ' '); //对s1的第一次引用，可变

        println!("s1: {}", s1,); //对s1的第二次引用，不可变
        println!("hello is: {}, s: {}", hello, s); //hello相当于是对s1的第一次引用，可变
    }

    // 该test有编译错误
    #[test]
    fn test_strtok_3_3() {
        let s = "hello world".to_owned();
        let mut s1 = s.as_str();
        let hello = strtok3(&mut s1, ' '); //对s1的第一次引用，可变

        println!("hello is: {}, s1: {}, s: {}", hello, s1, s); //hello等于第一次引用，可变；s1等于第二次引用，不可变
    }
}
```

### Example 2
```rust
// 错误，因为name指向的字符串会在函数结束时被drop掉
fn lifetime1() -> &str {
    let name = "Tyr".to_string();
    &name[1..]
}

// 错误，因为入参是一个有所有权的String，
// 那么在调用这个函数后String的所有权就被让渡给这个函数了
// 所以当这个函数结束后，这个String也会被drop掉
fn lifetime2(name: String) -> &str {
    &name[1..]
}

// 正确，因为Char的源代码是Chars<'a>
fn lifetime3(name: &str) -> Chars {
    name.chars()
}
```
