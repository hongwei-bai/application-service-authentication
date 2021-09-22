# application-service-auth

Authentication service application is providing a JWT based authentication for most of my service applications.

It supports a wide range of level access control, from public access to a user level access, then when necessary, it allows each application has a much more finer way to control their own access.

For example, for blog service application, a read/write and blog entry level access can be achieved with help of this service.


It has apis for generating token, refresh token, public token and apis to verify them.

It support a guest code generation and supports an auto expiration of guest code.

It has apis for login, logoff, and api-level controls for other service applications.

For example, if the user call a blog service api, it allows the blog service application to further verify user's privilege in real time.

guest admin controller is using a RESTFul way:

For example:

GuestController ``/admin``

```
    @PostMapping(path = ["/guest.do"])
    @ResponseBody
    fun addGuest(description: String, roles: String, expireTime: Long, sign: String?): ResponseEntity<*> =
            ResponseEntity.ok(guestAdminService.addGuest(description, expireTime))

    @DeleteMapping(path = ["/guest.do"])
    @ResponseBody
    fun delete(guestCode: String, sign: String?): ResponseEntity<*> =
            ResponseEntity.ok(guestAdminService.deleteGuest(guestCode))

    @GetMapping(path = ["/guest.do"])
    @ResponseBody
    fun getGuest(guestCode: String, sign: String?): ResponseEntity<*> =
            ResponseEntity.ok(guestAdminService.getGuest(guestCode))

    @PutMapping(path = ["/guest.do"])
    @ResponseBody
    fun updateUser(guestCode: String, expireTime: Long?, sign: String?): ResponseEntity<*> =
            ResponseEntity.ok(guestAdminService.updateGuest(guestCode, expireTime))
```

### API samples

#### Login API sample

``POST`` ``/authenticate/login.do``

request: 

```
{
    "userName": "hongwei",
    "credential": "pw_hashing_xxxxxxxxxxxxxxxxxxxxxx"
}
```

response: 

```
{
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJob25nd2VpIiwiZXhwIjoxNjMyMzA3ODgyLCJpYXQiOjE2MzIyOTcwODJ9.-lUiQP_x8158Ebbkqrwxxx",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJob25nd2VpIiwiZXhwIjoxNjM0ODg5MDgyLCJpYXQiOjE2MzIyOTcwODJ9.q1_2RdcMJbXeI2oGvNCxxx",
    "role": "user",
    "preferenceJson": "{\"locale\": \"zh-CN\", \"redirect\": \"/\"}",
    "privilegeJson": "{\"entries\": [\"blog\",\"knowledgeGraph\",\"admin\",\"ecommerce\",\"photo\",\"uploadExercise\",\"resume\",\"todo\",\"systemlogs\"],\"blog\": {\"create\": true,\"modAll\": true}, \"photo\": {\"all\": true}}"
}
```

#### Refresh token API sample

``POST`` ``/authenticate/refreshToken.do``

request: 

```
{
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJob25nd2VpIiwiZXhwIjoxNjM0OTEyMTEyLCJpYXQiOjE2MzIzMjAxMTJ9.q1_2RdcMJbXeI2oGvNCxxx"
}
```

response: 

```
{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJob25nd2VpIiwiZXhwIjoxNjMyMzMwOTkyLCJpYXQiOjE2MzIzMjAxOTJ9.PYwaRXjf_i0ONAqgoUG-lUiQP_x8158Ebbkqrwxxx"
}
```

#### Authorise API sample

``POST`` ``/authenticate/authorise.do``

request: 

```
{
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJob25nd2VpIiwiZXhwIjoxNjE3NDk0NDgyLCJpYXQiOjE2MTc0OTQ0MjJ9.xxxxxx"
}
```

failure response(expiration): 

```
{
    "timestamp": 1632320432609,
    "status": 500,
    "error": "Internal Server Error",
    "message": "JWT expired at 2021-04-04T08:01:22Z. Current time: 2021-09-22T22:20:32Z, a difference of 14825950280 milliseconds.  Allowed clock skew: 0 milliseconds.",
    "path": "/application-service-authentication/authenticate/authorise.do"
}
```

success response: 

```
{
    "validated": true,
    "validatedUntil": -1,
    "userName": "hongwei",
    "role": "user",
    "preferenceJson": "{\"locale\": \"zh-CN\", \"redirect\": \"/\"}",
    "privilegeJson": "{\"entries\": [\"blog\",\"knowledgeGraph\",\"admin\",\"ecommerce\",\"photo\",\"uploadExercise\",\"resume\",\"todo\",\"systemlogs\"],\"blog\": {\"create\": true,\"modAll\": true}, \"photo\": {\"all\": true}}"
}
```