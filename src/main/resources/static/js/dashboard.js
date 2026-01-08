document.addEventListener("DOMContentLoaded", initDashboard);

/* =========================
   INIT
========================= */
async function initDashboard() {
  const token = getToken();
  if (!token) return redirectToLogin();

  try {
    const posts = await fetchPosts(token);
    renderFeed(posts, token);
  } catch (e) {
    console.error("Dashboard error:", e);
  }
}

/* =========================
   AUTH HELPERS
========================= */
function getToken() {
  return localStorage.getItem("token");
}

function getCurrentUserId() {
  return Number(localStorage.getItem("userId"));
}

function redirectToLogin() {
  window.location.href = "/login";
}

/* =========================
   API
========================= */
async function fetchPosts(token) {
  const res = await fetch("/api/posts", {
    headers: { Authorization: `Bearer ${token}` }
  });
  if (!res.ok) throw new Error("Failed to load posts");
  const body = await res.json();
  return body.data || [];
}

/* =========================
   RENDER FEED
========================= */
function renderFeed(posts, token) {
  const feed = document.getElementById("feed");
  feed.innerHTML = "";

  posts.forEach(post => {
    const postEl = document.createElement("div");
    postEl.className = "post";
    postEl.dataset.postId = post.id;

    postEl.innerHTML = `
      <div class="post-header">
        <div class="profile-pic">
          <img src="https://i.pravatar.cc/40?img=${post.authorId % 50}">
        </div>
        <div>
          <span class="username">${post.username}</span>

          ${post.authorId !== getCurrentUserId()
        ? `<button class="follow-btn ${post.followingAuthor ? "following" : ""}"
                       id="follow-btn-${post.id}">
                 ${post.followingAuthor ? "Following" : "Follow"}
               </button>`
        : ""}

          <div class="post-time">${formatDate(post.createdAt)}</div>
        </div>
      </div>

      <div class="post-image">
        <img src="${post.imageUrl || randomImage(post.id)}">
      </div>

      <div class="post-actions">
        <button class="action-btn like-btn ${post.likedByCurrentUser ? "liked" : ""}"
                onclick="toggleLike(${post.id}, this)">
          ♥ <span class="like-count">${post.likeCount}</span>
        </button>

        <button class="action-btn"
                onclick="toggleCommentSection(${post.id})">
          💬 <span class="comment-count">${post.commentCount}</span>
        </button>
      </div>

      <div class="post-caption">
        <span class="username">${post.username}</span>
        ${post.caption || ""}
      </div>

      <div class="comments-section" id="comments-${post.id}" style="display:none">
  <div class="comments-list" id="comments-list-${post.id}"></div>

  <div class="add-comment">
    <input
      type="text"
      id="comment-input-${post.id}"
      placeholder="Add a comment..."
    />
    <button onclick="addComment(${post.id})">Post</button>
  </div>
</div>

    `;

    feed.appendChild(postEl);

    loadComments(post.id, token);

    // follow toggle
    const followBtn = document.getElementById(`follow-btn-${post.id}`);
    if (followBtn) {
      followBtn.onclick = () => toggleFollow(post, followBtn, token);
    }
  });
}

/* =========================
   FOLLOW
========================= */
async function toggleFollow(post, btn, token) {
  btn.disabled = true;

  const url = post.followingAuthor
    ? `/api/users/${post.authorId}/unfollow`
    : `/api/users/${post.authorId}/follow`;

  const method = post.followingAuthor ? "DELETE" : "POST";

  try {
    const res = await fetch(url, {
      method,
      headers: { Authorization: `Bearer ${token}` }
    });

    if (res.ok) {
      post.followingAuthor = !post.followingAuthor;
      btn.textContent = post.followingAuthor ? "Following" : "Follow";
      btn.classList.toggle("following", post.followingAuthor);
    }
  } finally {
    btn.disabled = false;
  }
}

/* =========================
   COMMENTS
========================= */
async function loadComments(postId, token) {
  const list = document.getElementById(`comments-list-${postId}`);
  if (!list) return;

  const res = await fetch(`/api/posts/${postId}/comments`, {
    headers: { Authorization: `Bearer ${token}` }
  });
  if (!res.ok) return;

  const body = await res.json();
  const comments = body.data || [];

  list.innerHTML = "";
  comments.forEach(c => {
    const div = document.createElement("div");
    div.className = "comment";
    div.innerHTML = `
      <span class="username">${c.username || "user"}</span>
      <span class="comment-text">${escapeHtml(c.content || "")}</span>
    `;
    list.appendChild(div);
  });
}

function toggleCommentSection(postId) {
  const el = document.getElementById(`comments-${postId}`);
  if (!el) return;
  el.style.display = el.style.display === "none" ? "block" : "none";
}

/* =========================
   LIKE
========================= */
async function toggleLike(postId, btn) {
  const token = getToken();
  const liked = btn.classList.contains("liked");

  const res = await fetch(`/api/posts/${postId}/${liked ? "unlike" : "like"}`, {
    method: liked ? "DELETE" : "POST",
    headers: { Authorization: `Bearer ${token}` }
  });

  if (!res.ok) return;

  btn.classList.toggle("liked");
  const countEl = btn.querySelector(".like-count");
  countEl.textContent = Number(countEl.textContent) + (liked ? -1 : 1);
}

/* =========================
   CREATE POST
========================= */
async function createPost() {
  const token = localStorage.getItem("token");
  if (!token) {
    window.location.href = "/login";
    return;
  }

  const captionEl = document.getElementById("post-caption");
  const imageEl = document.getElementById("post-image");

  if (!captionEl || !imageEl) {
    alert("Post input not found");
    return;
  }

  const caption = captionEl.value.trim();
  const imageUrl = imageEl.value.trim();

  if (!caption && !imageUrl) {
    alert("Please enter a caption or image URL");
    return;
  }

  const res = await fetch("/api/posts", {
    method: "POST",
    headers: {
      "Authorization": `Bearer ${token}`,
      "Content-Type": "application/json"
    },
    body: JSON.stringify({
      caption: caption,
      imageUrl: imageUrl
    })
  });

  if (!res.ok) {
    alert("Failed to create post");
    return;
  }

  // Clear inputs
  captionEl.value = "";
  imageEl.value = "";

  // Reload feed (simple & safe)
  location.reload();
}

/* =========================
   ADD COMMENT (GLOBAL)
========================= */
async function addComment(postId) {
  const token = localStorage.getItem("token");
  const input = document.getElementById(`comment-input-${postId}`);

  if (!input || !input.value.trim()) return;

  const res = await fetch(`/api/posts/${postId}/comments`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json"
    },
    body: JSON.stringify({
      content: input.value.trim()
    })
  });

  if (!res.ok) {
    alert("Failed to add comment");
    return;
  }

  input.value = "";
  loadComments(postId, token);
}



/* =========================
   UTILS
========================= */
function formatDate(date) {
  return new Date(date).toLocaleDateString();
}

function randomImage(id) {
  return `https://picsum.photos/600/400?random=${id}`;
}

function escapeHtml(str) {
  return String(str)
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;");
}
