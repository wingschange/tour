# Tour 后端接口文档

> Tour 是一款旅行体验分享应用，允许用户发布旅行照片、撰写游记，并探索他人分享的旅途故事。

---

## 目录

- [基础信息](#基础信息)
- [认证方式](#认证方式)
- [统一响应格式](#统一响应格式)
- [错误码说明](#错误码说明)
- [分页说明](#分页说明)
- [接口列表](#接口列表)
  - [认证模块 Auth](#认证模块-auth)
  - [用户模块 Users](#用户模块-users)
  - [游记模块 Posts](#游记模块-posts)
  - [照片模块 Photos](#照片模块-photos)
  - [评论模块 Comments](#评论模块-comments)
  - [点赞模块 Likes](#点赞模块-likes)
  - [标签模块 Tags](#标签模块-tags)
  - [目的地模块 Destinations](#目的地模块-destinations)
  - [搜索模块 Search](#搜索模块-search)

---

## 基础信息

| 属性 | 值 |
|---|---|
| 接口基础路径（生产） | `https://api.tour-app.com/v1` |
| 接口基础路径（本地） | `http://localhost:8080/v1` |
| 接口版本 | v1 |
| 数据格式 | JSON（文件上传接口使用 `multipart/form-data`） |
| 字符编码 | UTF-8 |
| 时间格式 | ISO 8601，例如 `2024-06-20T14:00:00Z` |

---

## 认证方式

本 API 采用 **JWT Bearer Token** 认证。

登录或注册成功后会返回 `accessToken`（访问令牌，有效期 **2 小时**）与 `refreshToken`（刷新令牌，有效期 **30 天**）。

在需要认证的接口中，请在请求头中携带：

```
Authorization: Bearer <access_token>
```

当 `accessToken` 过期时，调用 [刷新令牌](#post-authrefresh) 接口，使用 `refreshToken` 换取新的令牌对。

---

## 统一响应格式

**成功响应**（示例）

```json
{
  "id": "post_xyz789",
  "title": "三亚五日游",
  ...
}
```

**错误响应**

```json
{
  "code": "NOT_FOUND",
  "message": "请求的资源不存在"
}
```

**参数校验失败响应**

```json
{
  "code": "VALIDATION_ERROR",
  "message": "请求参数校验失败",
  "errors": [
    { "field": "email", "message": "必须是有效的邮箱地址" },
    { "field": "password", "message": "密码长度不能少于 8 位" }
  ]
}
```

---

## 错误码说明

| HTTP 状态码 | 业务 code | 含义 |
|---|---|---|
| 200 | — | 请求成功 |
| 201 | — | 创建成功 |
| 400 | `BAD_REQUEST` | 请求参数错误 |
| 401 | `UNAUTHORIZED` | 未认证或 Token 已过期 |
| 403 | `FORBIDDEN` | 无权限操作 |
| 404 | `NOT_FOUND` | 资源不存在 |
| 409 | `CONFLICT` | 资源冲突（如用户名已存在、重复点赞） |
| 422 | `VALIDATION_ERROR` | 请求体字段校验失败 |
| 500 | `INTERNAL_ERROR` | 服务器内部错误 |

---

## 分页说明

列表接口统一使用页码分页，请求参数：

| 参数 | 类型 | 默认值 | 说明 |
|---|---|---|---|
| `page` | integer | 1 | 页码，从 1 开始 |
| `pageSize` | integer | 20 | 每页条数，最大 100 |

响应结构：

```json
{
  "total": 1024,
  "page": 1,
  "pageSize": 20,
  "data": [ ... ]
}
```

---

## 接口列表

---

### 认证模块 Auth

#### POST `/auth/register` — 用户注册

创建新用户账号，注册成功后返回令牌对。

**请求体**

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| `username` | string | ✅ | 用户名，3–30 位，仅允许字母、数字、下划线 |
| `email` | string | ✅ | 邮箱地址 |
| `password` | string | ✅ | 密码，8–72 位，须包含字母和数字 |

**请求示例**

```json
{
  "username": "traveler_zhang",
  "email": "zhang@example.com",
  "password": "P@ssw0rd123"
}
```

**响应示例（201）**

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4...",
  "expiresIn": 7200,
  "user": {
    "id": "user_123abc",
    "username": "traveler_zhang",
    "email": "zhang@example.com",
    "bio": null,
    "avatarUrl": null,
    "followersCount": 0,
    "followingCount": 0,
    "postsCount": 0,
    "createdAt": "2024-06-20T08:00:00Z"
  }
}
```

---

#### POST `/auth/login` — 用户登录

**请求体**

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| `email` | string | ✅ | 邮箱地址 |
| `password` | string | ✅ | 密码 |

**响应示例（200）** — 同注册响应格式。

---

#### POST `/auth/logout` — 用户登出 🔒

使当前令牌失效。

**响应示例（200）**

```json
{ "message": "登出成功" }
```

---

#### POST `/auth/refresh` — 刷新访问令牌

**请求体**

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| `refreshToken` | string | ✅ | 登录时下发的刷新令牌 |

**响应示例（200）**

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "bmV3cmVmcmVzaHRva2Vu...",
  "expiresIn": 7200
}
```

---

#### POST `/auth/forgot-password` — 发送密码重置邮件

**请求体**

```json
{ "email": "zhang@example.com" }
```

**响应示例（200）**

```json
{ "message": "密码重置邮件已发送，请查收" }
```

---

#### POST `/auth/reset-password` — 重置密码

**请求体**

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| `token` | string | ✅ | 邮件中的重置令牌 |
| `newPassword` | string | ✅ | 新密码，8–72 位 |

---

### 用户模块 Users

#### GET `/users/me` — 获取当前用户信息 🔒

**响应示例（200）**

```json
{
  "id": "user_123abc",
  "username": "traveler_zhang",
  "email": "zhang@example.com",
  "bio": "热爱旅行，走遍天涯海角。",
  "avatarUrl": "https://cdn.tour-app.com/avatars/user_123.jpg",
  "followersCount": 320,
  "followingCount": 180,
  "postsCount": 45,
  "createdAt": "2024-03-15T08:30:00Z"
}
```

---

#### PUT `/users/me` — 更新当前用户信息 🔒

**请求体**

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| `username` | string | ❌ | 新用户名 |
| `bio` | string \| null | ❌ | 个人简介，最长 200 字 |

---

#### POST `/users/me/avatar` — 上传头像 🔒

**Content-Type：** `multipart/form-data`

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| `file` | binary | ✅ | 图片文件，支持 JPEG/PNG/WebP，最大 5 MB |

**响应示例（200）**

```json
{ "avatarUrl": "https://cdn.tour-app.com/avatars/user_123.jpg" }
```

---

#### PUT `/users/me/password` — 修改密码 🔒

**请求体**

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| `currentPassword` | string | ✅ | 当前密码 |
| `newPassword` | string | ✅ | 新密码，8–72 位 |

---

#### GET `/users/{userId}` — 获取指定用户公开信息

**路径参数：** `userId` — 用户 ID

**响应示例（200）**

```json
{
  "id": "user_123abc",
  "username": "traveler_zhang",
  "bio": "热爱旅行，走遍天涯海角。",
  "avatarUrl": "https://cdn.tour-app.com/avatars/user_123.jpg",
  "followersCount": 320,
  "followingCount": 180,
  "postsCount": 45,
  "isFollowing": false
}
```

---

#### GET `/users/{userId}/followers` — 获取粉丝列表

**查询参数：** `page`、`pageSize`

---

#### GET `/users/{userId}/following` — 获取关注列表

**查询参数：** `page`、`pageSize`

---

#### POST `/users/{userId}/follow` — 关注用户 🔒

**响应（200）**：`{ "message": "关注成功" }`

**错误情况：**
- `400`：不能关注自己
- `409`：已关注该用户

---

#### DELETE `/users/{userId}/follow` — 取消关注用户 🔒

**响应（200）**：`{ "message": "取消关注成功" }`

---

### 游记模块 Posts

#### GET `/posts` — 获取游记列表（发现页）

**查询参数**

| 参数 | 类型 | 默认值 | 说明 |
|---|---|---|---|
| `page` | integer | 1 | 页码 |
| `pageSize` | integer | 20 | 每页条数 |
| `sort` | string | `latest` | `latest`（最新）/ `popular`（最热）/ `following`（关注，需登录） |
| `tag` | string | — | 按标签过滤 |
| `destination` | string | — | 按目的地 ID 过滤 |

**响应示例（200）**

```json
{
  "total": 1024,
  "page": 1,
  "pageSize": 20,
  "data": [
    {
      "id": "post_xyz789",
      "title": "三亚五日游——阳光与沙滩",
      "summary": "这是我第一次来三亚，海水蔚蓝，沙滩细腻...",
      "coverPhotoUrl": "https://cdn.tour-app.com/photos/photo_abc123_cover.jpg",
      "author": { "id": "user_123abc", "username": "traveler_zhang", ... },
      "destination": { "id": "dest_sanya", "name": "三亚", "country": "中国" },
      "tags": [{ "name": "海滩", "postsCount": 2048 }],
      "likesCount": 128,
      "commentsCount": 34,
      "bookmarksCount": 56,
      "isLiked": false,
      "isBookmarked": false,
      "isPublic": true,
      "createdAt": "2024-06-20T14:00:00Z",
      "updatedAt": "2024-06-21T09:15:00Z"
    }
  ]
}
```

---

#### POST `/posts` — 发布新游记 🔒

**请求体**

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| `title` | string | ✅ | 游记标题，2–100 字 |
| `content` | string | ✅ | 正文（支持 Markdown），最长 50000 字 |
| `coverPhotoId` | string \| null | ❌ | 封面照片 ID（须已上传） |
| `photoIds` | string[] | ❌ | 引用照片 ID 列表，最多 50 张 |
| `destinationId` | string \| null | ❌ | 目的地 ID |
| `tags` | string[] | ❌ | 标签列表，最多 10 个，不存在的标签自动创建 |
| `isPublic` | boolean | ❌ | 是否公开，默认 `true` |

---

#### GET `/posts/{postId}` — 获取游记详情

**路径参数：** `postId`

**响应示例（200）** — 在列表字段基础上额外返回：

```json
{
  ...
  "content": "## 第一天\n抵达三亚，入住酒店...",
  "photos": [
    {
      "id": "photo_abc123",
      "url": "https://cdn.tour-app.com/photos/photo_abc123.jpg",
      "thumbnailUrl": "https://cdn.tour-app.com/photos/photo_abc123_thumb.jpg",
      "width": 3024,
      "height": 4032,
      "sizeBytes": 2457600,
      "uploadedAt": "2024-06-20T13:45:00Z"
    }
  ]
}
```

---

#### PUT `/posts/{postId}` — 更新游记 🔒

仅游记作者可修改。**请求体字段同发布接口，均为选填。**

---

#### DELETE `/posts/{postId}` — 删除游记 🔒

仅游记作者或管理员可删除。

**响应（200）**：`{ "message": "删除成功" }`

---

#### GET `/users/{userId}/posts` — 获取指定用户发布的游记列表

**查询参数：** `page`、`pageSize`

---

#### GET `/users/me/bookmarks` — 获取收藏的游记列表 🔒

**查询参数：** `page`、`pageSize`

---

#### POST `/posts/{postId}/bookmark` — 收藏游记 🔒

**响应（200）**：`{ "message": "收藏成功" }`

---

#### DELETE `/posts/{postId}/bookmark` — 取消收藏游记 🔒

**响应（200）**：`{ "message": "取消收藏成功" }`

---

### 照片模块 Photos

#### POST `/photos` — 上传照片 🔒

支持批量上传，每次最多 **10 张**，单张最大 **20 MB**。
支持格式：JPEG、PNG、WebP、HEIC。

**Content-Type：** `multipart/form-data`

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| `files` | binary[] | ✅ | 照片文件列表 |

**响应示例（201）**

```json
{
  "photos": [
    {
      "id": "photo_abc123",
      "url": "https://cdn.tour-app.com/photos/photo_abc123.jpg",
      "thumbnailUrl": "https://cdn.tour-app.com/photos/photo_abc123_thumb.jpg",
      "width": 3024,
      "height": 4032,
      "sizeBytes": 2457600,
      "uploadedAt": "2024-06-20T13:45:00Z"
    }
  ]
}
```

---

#### GET `/photos/{photoId}` — 获取照片详情

**响应示例（200）** — 同上方 Photo 对象结构。

---

#### DELETE `/photos/{photoId}` — 删除照片 🔒

仅照片所有者或管理员可删除。

**响应（200）**：`{ "message": "删除成功" }`

---

### 评论模块 Comments

#### GET `/posts/{postId}/comments` — 获取游记评论列表

返回顶层评论，每条评论附带最多 3 条热门子回复。

**查询参数：** `page`、`pageSize`

**响应示例（200）**

```json
{
  "total": 34,
  "page": 1,
  "pageSize": 20,
  "data": [
    {
      "id": "comment_def456",
      "content": "写得太好了！让我也想去三亚了。",
      "author": { "id": "user_456def", "username": "wanderer_li", ... },
      "parentId": null,
      "replyToUser": null,
      "likesCount": 12,
      "isLiked": false,
      "repliesCount": 3,
      "replies": [ ... ],
      "createdAt": "2024-06-22T10:00:00Z"
    }
  ]
}
```

---

#### POST `/posts/{postId}/comments` — 发表评论 🔒

**请求体**

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| `content` | string | ✅ | 评论内容，1–500 字 |
| `parentId` | string \| null | ❌ | 回复某条评论时传入父评论 ID |

---

#### GET `/comments/{commentId}/replies` — 获取评论子回复列表

**查询参数：** `page`、`pageSize`

---

#### DELETE `/comments/{commentId}` — 删除评论 🔒

仅评论作者或管理员可删除。删除后其所有子回复也将被一并删除。

**响应（200）**：`{ "message": "删除成功" }`

---

### 点赞模块 Likes

#### POST `/posts/{postId}/like` — 点赞游记 🔒

**响应示例（200）**

```json
{ "likesCount": 128 }
```

---

#### DELETE `/posts/{postId}/like` — 取消点赞游记 🔒

**响应示例（200）**

```json
{ "likesCount": 127 }
```

---

#### POST `/comments/{commentId}/like` — 点赞评论 🔒

**响应示例（200）**

```json
{ "likesCount": 13 }
```

---

#### DELETE `/comments/{commentId}/like` — 取消点赞评论 🔒

**响应示例（200）**

```json
{ "likesCount": 12 }
```

---

### 标签模块 Tags

#### GET `/tags` — 获取热门标签列表

**查询参数**

| 参数 | 类型 | 默认值 | 说明 |
|---|---|---|---|
| `limit` | integer | 20 | 返回数量，最大 100 |

**响应示例（200）**

```json
{
  "tags": [
    { "name": "海滩", "postsCount": 2048 },
    { "name": "徒步", "postsCount": 1536 },
    { "name": "美食", "postsCount": 3072 }
  ]
}
```

---

#### GET `/tags/{tagName}/posts` — 获取标签下的游记列表

**路径参数：** `tagName` — 标签名称（如 `海滩`）

**查询参数：** `page`、`pageSize`、`sort`（`latest` / `popular`）

---

### 目的地模块 Destinations

#### GET `/destinations` — 获取目的地列表

**查询参数**

| 参数 | 类型 | 说明 |
|---|---|---|
| `keyword` | string | 关键词搜索（城市、景点名称等） |
| `page` | integer | 页码 |
| `pageSize` | integer | 每页条数 |

**响应示例（200）**

```json
{
  "total": 500,
  "data": [
    {
      "id": "dest_sanya",
      "name": "三亚",
      "country": "中国",
      "coverPhotoUrl": "https://cdn.tour-app.com/destinations/dest_sanya_cover.jpg",
      "postsCount": 5120
    }
  ]
}
```

---

#### GET `/destinations/{destinationId}` — 获取目的地详情

**响应示例（200）**

```json
{
  "id": "dest_sanya",
  "name": "三亚",
  "country": "中国",
  "coverPhotoUrl": "https://cdn.tour-app.com/destinations/dest_sanya_cover.jpg",
  "postsCount": 5120,
  "description": "三亚，海南省地级市，素有"东方夏威夷"之称...",
  "latitude": 18.2528,
  "longitude": 109.5119,
  "popularTags": [
    { "name": "海滩", "postsCount": 2048 },
    { "name": "潜水", "postsCount": 768 }
  ]
}
```

---

#### GET `/destinations/{destinationId}/posts` — 获取目的地相关游记列表

**查询参数：** `page`、`pageSize`、`sort`（`latest` / `popular`）

---

### 搜索模块 Search

#### GET `/search` — 全局搜索

按关键词同时搜索游记、用户和目的地，返回各类型的 Top 结果。

**查询参数**

| 参数 | 类型 | 必填 | 说明 |
|---|---|---|---|
| `q` | string | ✅ | 搜索关键词 |
| `type` | string | ❌ | 限定类型：`posts` / `users` / `destinations`，不填返回所有 |
| `page` | integer | ❌ | 页码 |
| `pageSize` | integer | ❌ | 每页条数 |

**响应示例（200）**

```json
{
  "query": "三亚",
  "posts": {
    "total": 512,
    "data": [ { ...Post } ]
  },
  "users": {
    "total": 18,
    "data": [ { ...UserPublicProfile } ]
  },
  "destinations": {
    "total": 3,
    "data": [ { ...Destination } ]
  }
}
```

---

## 图标说明

- 🔒 — 该接口需要登录认证（请求头携带 `Authorization: Bearer <token>`）

---

## 机器可读规范

本文档对应的 OpenAPI 3.0 规范文件位于项目根目录：

```
swagger.yaml
```

可使用以下工具在线预览：
- [Swagger Editor](https://editor.swagger.io/) — 将 `swagger.yaml` 内容粘贴即可渲染
- [Swagger UI](https://swagger.io/tools/swagger-ui/) — 可集成到后端服务中提供在线调试
- [Redoc](https://redocly.com/redoc/) — 生成美观的文档站点
