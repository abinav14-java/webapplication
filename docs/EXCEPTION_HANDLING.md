# Centralized Exception Handling - Implementation Guide

## Overview
Your application now has a centralized exception handling system using Spring's `@ControllerAdvice`. This eliminates scattered try-catch blocks and provides consistent error responses.

## How It Works

### 1. Custom Exceptions
Four custom exceptions are available:

```java
// When a resource (user, post, etc.) is not found
throw new ResourceNotFoundException("Post with ID 123 not found");

// When authentication fails (login, invalid credentials)
throw new AuthenticationException("Invalid username or password");

// When user lacks permission for an action
throw new UnauthorizedException("You cannot delete this post");

// When input validation fails
throw new ValidationException("Email format is invalid");
```

### 2. GlobalExceptionHandler
Automatically catches all exceptions and returns standardized JSON responses:

```json
{
  "status": 404,
  "message": "Post with ID 123 not found",
  "error": "Resource Not Found",
  "path": "/api/posts/123",
  "timestamp": "2026-02-02T12:30:45"
}
```

## Before & After Examples

### ❌ OLD WAY (Scattered try-catch)
```java
@PostMapping
public ResponseEntity<?> createPost(@RequestBody Post post) {
    try {
        String userEmail = getCurrentUserEmail();
        Users user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new Exception("User not found"));
        
        post.setUser(user);
        Post savedPost = postService.createPost(post);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Post created successfully", savedPost));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse("Error creating post: " + e.getMessage(), null));
    }
}
```

### ✅ NEW WAY (Centralized handling)
```java
@PostMapping
public ResponseEntity<?> createPost(@RequestBody Post post) {
    String userEmail = getCurrentUserEmail();
    Users user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    
    post.setUser(user);
    Post savedPost = postService.createPost(post);
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(new ApiResponse("Post created successfully", savedPost));
}
```

**Much cleaner!** No try-catch needed. Exceptions are handled automatically.

## Usage Examples

### Example 1: PostController - Create Post
```java
@PostMapping
public ResponseEntity<?> createPost(@RequestBody Post post) {
    String userEmail = getCurrentUserEmail();
    
    // This automatically throws ResourceNotFoundException if user not found
    Users user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "User with email " + userEmail + " not found"));
    
    if (post.getCaption() == null || post.getCaption().trim().isEmpty()) {
        throw new ValidationException("Post caption cannot be empty");
    }
    
    post.setUser(user);
    Post savedPost = postService.createPost(post);
    
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(new ApiResponse("Post created successfully", savedPost));
}
```

### Example 2: PostController - Delete Post
```java
@DeleteMapping("/{id}")
public ResponseEntity<?> deletePost(@PathVariable Long id) {
    String userEmail = getCurrentUserEmail();
    
    Post post = postService.getPostById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Post with ID " + id + " not found"));
    
    if (!post.getUser().getEmail().equals(userEmail)) {
        throw new UnauthorizedException(
                "You are not authorized to delete this post");
    }
    
    postService.deletePost(id);
    return ResponseEntity.ok(new ApiResponse("Post deleted successfully", null));
}
```

### Example 3: UserController - Login
```java
@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    if (request.getEmail() == null || request.getPassword() == null) {
        throw new ValidationException(
                "Email and password are required");
    }
    
    Users user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new AuthenticationException(
                    "Invalid username or password"));
    
    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
        throw new AuthenticationException(
                "Invalid username or password");
    }
    
    String token = jwtUtil.generateToken(request.getEmail());
    return ResponseEntity.ok(new ApiResponse("Login successful", token));
}
```

## Exception Mapping

| Exception | HTTP Status | Use When |
|-----------|-------------|----------|
| `ResourceNotFoundException` | 404 | Resource (user, post) not found |
| `AuthenticationException` | 401 | Login fails, invalid credentials |
| `UnauthorizedException` | 403 | User lacks permission |
| `ValidationException` | 400 | Input validation fails |
| `UsernameNotFoundException` | 401 | Spring Security - user not found |
| `BadCredentialsException` | 401 | Spring Security - wrong password |
| Any other `Exception` | 500 | Unexpected errors |

## Error Response Format

All errors are returned as JSON:
```json
{
  "status": 404,
  "message": "Post with ID 999 not found",
  "error": "Resource Not Found",
  "path": "/api/posts/999",
  "timestamp": "2026-02-02T14:25:30.123456"
}
```

## Benefits

✅ **Cleaner Code** - No more try-catch blocks scattered everywhere
✅ **Consistent Errors** - All errors follow the same format
✅ **Better Logging** - All exceptions are logged automatically
✅ **Easier Maintenance** - Change error handling in one place
✅ **Type Safety** - Specific exceptions for specific errors
✅ **HTTP Standards** - Correct status codes for each error type

## Next Steps

1. Replace try-catch blocks in controllers with custom exceptions
2. Use specific exceptions instead of generic `Exception`
3. Let `GlobalExceptionHandler` handle all error responses
4. Test your endpoints to see the standardized error responses

## Example Response Tests

```bash
# Test 404 - Post not found
curl http://localhost:8080/api/posts/999
# Returns: {"status": 404, "message": "Post not found", ...}

# Test 400 - Validation error
curl -X POST http://localhost:8080/api/posts \
  -H "Content-Type: application/json" \
  -d '{"caption": ""}'
# Returns: {"status": 400, "message": "Post caption cannot be empty", ...}

# Test 401 - Unauthorized
curl -X DELETE http://localhost:8080/api/posts/1 \
  -H "Authorization: Bearer invalid_token"
# Returns: {"status": 401, "message": "Invalid token", ...}

# Test 403 - Forbidden
curl -X DELETE http://localhost:8080/api/posts/1 \
  -H "Authorization: Bearer other_users_token"
# Returns: {"status": 403, "message": "Not authorized to delete", ...}
```
