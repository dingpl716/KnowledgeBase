## Creating Cookies

1. Javascript - `document.cookie`
2. Web server - Set-cookie header

## Cookie Properties

### Sent with every request
### Cookie Scope
  - Domain
    
    Set cookie for all domains that ends with `example.com`  
    `document.cookie="id=123456; domain=.example.com"`
  - Path
    
    Set cookie for `/path1`
    `document.cookie="id=123456; path=/path1"`

### Expires, `Max-age` header
### Same site, 
  Used to deal with cross sites situation. For example, there are two domains A and B. On website B, there could be images pointing to domain A, or links jumping to domain A. So if user opens domain B, or click the link jumping to domain A from B, the `samesite` property will be used to determine wether or not send the cookies belongs to domain A to domain A.
  - `strict`: Never send the cookies of domain A to domain A if the user is visiting domain B.
  - `lax`: Send the cookies of domain A to domain A if the user clicks the link and jumps to domain A (**the link must be a GET request, if it is a POST request, cookies will not be sent**). But do not send cookies of domain A to domain A if domain B just makes a request to open an image from domain A. (简单的讲就是，如果涉及到人为操作，比如点击链接跳转到domain A，那么就发送domain A的cookies。如果是自动的请求某些资源，自动的发送request，比如拿一张图片，那么就不发送domain A的cookies)
  - `none`: Always send cookies of domain A to domain A if user is visiting domain B.
  - Does not set this property
    - Before Chrome 84, it is treated like `none`
    - After Chrome 84, it is treated like `lax`

## Cookie Types

- Session
- Permanent
- Httponly
  - Cookies that can only be set by server.
  - Cannot access from Javascript, but can be seen in the response header.
- Secure
  - Only available for sites that are HTTPS.
- Third party
  - 
- Zombie

## Cookie Security
- Stealing cookies
- Cross site request forgery
