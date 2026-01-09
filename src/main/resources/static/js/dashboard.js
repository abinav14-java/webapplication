/* ========================================
   INSTACLONE - ENHANCED DASHBOARD
   ======================================== */

// Cache elements
let cachedToken, cachedUserId, cachedUsername;
let currentPage = 0;
let isLoading = false;
let hasMorePosts = true;

document.addEventListener("DOMContentLoaded", initDashboard);

/* =========================
   INITIALIZATION
   ========================= */
async function initDashboard() {
  const token = getToken();
  if (!token) return redirectToLogin();

  cachedToken = token;
  cachedUserId = +localStorage.getItem("userId");
  cachedUsername = localStorage.getItem("username");

  try {
    // Initialize theme
    initTheme();

    // Initialize sidebar buttons
    initSidebarButtons();

    // Load initial posts
    const posts = await fetchPosts();
    renderFeed(posts);

    // Initialize infinite scroll
    initInfiniteScroll();

    // Update user stats
    await updateUserStats();
  } catch (e) {
    console.error("Dashboard error:", e);
    showToast("Failed to load dashboard", "error");
  }
}

/* =========================
   THEME MANAGEMENT
   ========================= */
function initTheme() {
  const savedTheme = localStorage.getItem("theme") || "light";
  if (savedTheme === "dark") {
    document.body.classList.add("dark-mode");
    updateThemeButton();
  }
}

function toggleTheme() {
  document.body.classList.toggle("dark-mode");
  const isDark = document.body.classList.contains("dark-mode");
  localStorage.setItem("theme", isDark ? "dark" : "light");
  updateThemeButton();
}

function updateThemeButton() {
  const btn = document.getElementById("sidebarThemeBtn");
  if (btn) btn.textContent = document.body.classList.contains("dark-mode") ? "☀️" : "🌙";
}

/* =========================
   TOAST NOTIFICATIONS
   ========================= */
function showToast(message, type = "info") {
  const container = document.getElementById("toast-container");
  const toast = document.createElement("div");
  toast.className = `toast ${type}`;
  toast.textContent = message;
  container.appendChild(toast);

  setTimeout(() => {
    toast.classList.add("removing");
    setTimeout(() => toast.remove(), 300);
  }, 3000);
}

/* =========================
   SIDEBAR BUTTONS
   ========================= */
function initSidebarButtons() {
  const themeBtn = document.getElementById("sidebarThemeBtn");
  const profileBtn = document.getElementById("sidebarProfileBtn");
  const searchBtn = document.getElementById("sidebarSearchBtn");
  const logoutBtn = document.getElementById("sidebarLogoutBtn");
  const createBtn = document.getElementById("sidebarCreateBtn");
  const fileInput = document.getElementById("sidebarPostInput");

  themeBtn?.addEventListener("click", toggleTheme);
  profileBtn?.addEventListener("click", showMyProfile);
  searchBtn?.addEventListener("click", toggleSearchBar);
  logoutBtn?.addEventListener("click", logout);
  createBtn?.addEventListener("click", () => fileInput?.click());

  // File input for post creation
  fileInput?.addEventListener("change", handleFileSelect);
}

function toggleSearchBar() {
  const searchSection = document.getElementById("search-section");
  const dashboardContent = document.getElementById("dashboard-content");
  const searchInput = document.getElementById("search-input");

  searchSection.style.display = searchSection.style.display === "none" ? "block" : "none";
  dashboardContent.style.display = dashboardContent.style.display === "none" ? "block" : "none";

  if (searchSection.style.display === "block") {
    searchInput.focus();
    initSearchListener();
  }
}

function initSearchListener() {
  const searchInput = document.getElementById("search-input");
  searchInput.addEventListener("input", debounce(handleSearch, 300));
}

function debounce(func, wait) {
  let timeout;
  return function executedFunction(...args) {
    const later = () => {
      clearTimeout(timeout);
      func(...args);
    };
    clearTimeout(timeout);
    timeout = setTimeout(later, wait);
  };
}

async function handleSearch(e) {
  const query = e.target.value.trim().toLowerCase();
  const resultsDiv = document.getElementById("search-results");

  if (!query) {
    resultsDiv.style.display = "none";
    resultsDiv.innerHTML = "";
    return;
  }

  try {
    const posts = await fetchPosts(100);

    // Filter by caption or hashtags
    const filtered = posts.filter(post =>
      post.caption?.toLowerCase().includes(query) ||
      extractHashtags(post.caption).some(tag => tag.includes(query.replace("#", "")))
    );

    if (filtered.length === 0) {
      resultsDiv.innerHTML = "<div style='padding: 16px; text-align: center; color: #999;'>No results found</div>";
    } else {
      resultsDiv.innerHTML = filtered.map(post => `
        <div class="search-result-item" onclick="scrollToPost(${post.id})">
          <img src="${post.imageUrl || randomImage(post.id)}" style="width: 40px; height: 40px; border-radius: 8px; object-fit: cover;">
          <div>
            <strong>${post.username}</strong><br>
            <span style="font-size: 12px; color: #666;">${post.caption?.substring(0, 50)}...</span>
          </div>
        </div>
      `).join("");
    }
    resultsDiv.style.display = "block";
  } catch (e) {
    console.error("Search error:", e);
  }
}

function scrollToPost(postId) {
  const postEl = document.querySelector(`[data-post-id="${postId}"]`);
  if (postEl) {
    postEl.scrollIntoView({ behavior: "smooth", block: "center" });
    postEl.style.boxShadow = "0 0 20px rgba(102, 126, 234, 0.5)";
    setTimeout(() => postEl.style.boxShadow = "", 2000);
  }
}

function logout() {
  if (confirm("Are you sure you want to logout?")) {
    localStorage.removeItem("token");
    localStorage.removeItem("userId");
    localStorage.removeItem("username");
    window.location.href = "/login";
  }
}

/* =========================
   AUTH HELPERS
   ========================= */
function getToken() {
  return localStorage.getItem("token");
}

function redirectToLogin() {
  window.location.href = "/login";
}

/* =========================
   API CALLS
   ========================= */
async function fetchPosts(limit = 10) {
  try {
    const res = await fetch(`/api/posts`, {
      headers: { Authorization: `Bearer ${cachedToken}` }
    });
    if (!res.ok) throw new Error("Failed to load posts");
    const body = await res.json();
    return body.data || [];
  } catch (e) {
    console.error("Fetch posts error:", e);
    return [];
  }
}

async function updateUserStats() {
  try {
    const [followers, following] = await Promise.all([
      fetch(`/api/users/${cachedUserId}/followers/count`, {
        headers: { Authorization: `Bearer ${cachedToken}` }
      }).then(r => r.json()),
      fetch(`/api/users/${cachedUserId}/following/count`, {
        headers: { Authorization: `Bearer ${cachedToken}` }
      }).then(r => r.json())
    ]);

    const posts = await fetchPosts(0, 100);
    const userPosts = posts.filter(p => p.authorId === cachedUserId).length;

    document.getElementById("postCount").textContent = userPosts;
    document.getElementById("followerCount").textContent = followers.count || 0;
    document.getElementById("followingCount").textContent = following.count || 0;
  } catch (e) {
    console.error("Update stats error:", e);
  }
}

/* =========================
   PROFILE PAGE
   ========================= */
async function showMyProfile() {
  const profileDiv = document.getElementById("profile-page");
  const dashboardContent = document.getElementById("dashboard-content");
  const searchSection = document.getElementById("search-section");

  searchSection.style.display = "none";
  dashboardContent.style.display = "none";

  try {
    const posts = await fetchPosts(100);
    const userPosts = posts.filter(p => p.authorId === cachedUserId);
    const [followers, following] = await Promise.all([
      fetch(`/api/users/${cachedUserId}/followers/count`, {
        headers: { Authorization: `Bearer ${cachedToken}` }
      }).then(r => r.json()),
      fetch(`/api/users/${cachedUserId}/following/count`, {
        headers: { Authorization: `Bearer ${cachedToken}` }
      }).then(r => r.json())
    ]);

    profileDiv.innerHTML = `
      <button onclick="hideProfile()" style="margin-bottom: 16px; padding: 8px 16px; border: none; background: #f0f0f0; border-radius: 6px; cursor: pointer; font-weight: 600;">← Back to Feed</button>
      <div class="profile-page">
        <div class="profile-header">
          <div class="profile-avatar">👤</div>
          <div class="profile-info">
            <h2>${cachedUsername}</h2>
            <p>User ID: ${cachedUserId}</p>
            <div class="profile-stats">
              <div>
                <strong>${userPosts.length}</strong>
                <span>Posts</span>
              </div>
              <div>
                <strong>${followers.count || 0}</strong>
                <span>Followers</span>
              </div>
              <div>
                <strong>${following.count || 0}</strong>
                <span>Following</span>
              </div>
            </div>
          </div>
        </div>

        <h3 style="margin-top: 32px; margin-bottom: 16px; font-size: 18px;">My Posts</h3>
        <div style="display: grid; grid-template-columns: repeat(auto-fill, minmax(150px, 1fr)); gap: 16px;">
          ${userPosts.map(post => `
            <div onclick="scrollToPost(${post.id})" style="cursor: pointer; position: relative; overflow: hidden; border-radius: 8px;">
              <img src="${post.imageUrl || randomImage(post.id)}" style="width: 100%; height: 150px; object-fit: cover;">
              <div style="position: absolute; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; color: white; opacity: 0; transition: opacity 0.2s; font-weight: 600;">
                💬 ${post.commentCount} ♥ ${post.likeCount}
              </div>
            </div>
          `).join("")}
        </div>
      </div>
    `;

    profileDiv.style.display = "block";
  } catch (e) {
    console.error("Profile error:", e);
    showToast("Failed to load profile", "error");
  }
}

function hideProfile() {
  document.getElementById("profile-page").style.display = "none";
  document.getElementById("dashboard-content").style.display = "block";
}

/* =========================
   RENDER FEED
   ========================= */
function renderFeed(posts, append = false) {
  const feed = document.getElementById("feed");

  if (!append) feed.innerHTML = "";

  posts.forEach(post => {
    const postEl = document.createElement("div");
    postEl.className = "post";
    postEl.dataset.postId = post.id;

    const isOwner = post.authorId === cachedUserId;
    const menuHTML = isOwner ? `
      <div style="position: relative;">
        <button class="post-menu-btn" onclick="togglePostMenu(event, ${post.id})" style="background: none; border: none; font-size: 20px; cursor: pointer; color: #666; padding: 8px;">⋯</button>
        <div id="menu-${post.id}" class="post-menu" style="display: none;">
          <button onclick="editPost(${post.id})">Edit Caption</button>
          <button class="danger" onclick="deletePost(${post.id})">Delete Post</button>
        </div>
      </div>
    ` : "";

    postEl.innerHTML = `
      <div class="post-header">
        <div class="profile-pic">
          <img src="https://i.pravatar.cc/40?img=${post.authorId % 50}">
        </div>
        <div style="flex: 1;">
          <span class="username">${post.username}</span>
          ${post.authorId !== cachedUserId
        ? `<button class="follow-btn ${post.followingAuthor ? "following" : ""}"
                     id="follow-btn-${post.id}">
                ${post.followingAuthor ? "Following" : "Follow"}
              </button>`
        : ""}
          <div class="post-time">${formatDate(post.createdAt)}${post.updatedAt && post.updatedAt !== post.createdAt ? ' (edited)' : ''}</div>
        </div>
        ${menuHTML}
      </div>

      <div class="post-image">
        <img src="${post.imageUrl || randomImage(post.id)}" loading="lazy">
      </div>

      <div class="post-actions">
        <button class="action-btn like-btn ${post.likedByCurrentUser ? "liked" : ""}"
                onclick="toggleLike(${post.id}, this)">
          ♥ <span class="like-count">${post.likeCount}</span>
        </button>
        <button class="action-btn" onclick="toggleCommentSection(${post.id})">
          💬 <span class="comment-count">${post.commentCount}</span>
        </button>
      </div>

      <div class="post-caption">
        <span class="username">${post.username}</span>
        ${renderCaption(post.caption || "")}
      </div>

      <div class="comments-section" id="comments-${post.id}" style="display:none">
        <div class="comments-list" id="comments-list-${post.id}"></div>
        <div class="add-comment">
          <input type="text" id="comment-input-${post.id}" placeholder="Add a comment...">
          <button onclick="addComment(${post.id})">Post</button>
        </div>
      </div>
    `;

    feed.appendChild(postEl);

    // Load comments
    loadComments(post.id);

    // Follow button listener
    const followBtn = document.getElementById(`follow-btn-${post.id}`);
    if (followBtn) {
      followBtn.onclick = () => toggleFollow(post, followBtn);
    }
  });

  hasMorePosts = posts.length >= 10;
}

function renderCaption(caption) {
  let html = caption
    .replace(/\n/g, "<br>")
    .replace(/#(\w+)/g, '<span class="hashtag" onclick="event.stopPropagation(); searchByHashtag(\'$1\')">#$1</span>')
    .replace(/@(\w+)/g, '<span style="color: #667eea; font-weight: 600;">@$1</span>');
  return html;
}

function searchByHashtag(tag) {
  const searchInput = document.getElementById("search-input");
  searchInput.value = "#" + tag;
  toggleSearchBar();
  handleSearch({ target: searchInput });
}

/* =========================
   INFINITE SCROLL
   ========================= */
function initInfiniteScroll() {
  window.addEventListener("scroll", debounce(() => {
    if ((window.innerHeight + window.scrollY) >= document.body.offsetHeight - 500) {
      if (!isLoading && hasMorePosts) {
        loadMorePosts();
      }
    }
  }, 200));
}

async function loadMorePosts() {
  if (isLoading) return;

  isLoading = true;
  const loader = document.getElementById("loader");
  loader.style.display = "block";

  try {
    currentPage++;
    const posts = await fetchPosts();
    renderFeed(posts, true);
  } catch (e) {
    console.error("Load more error:", e);
  } finally {
    isLoading = false;
    loader.style.display = "none";
  }
}

/* =========================
   POST ACTIONS
   ========================= */
function togglePostMenu(e, postId) {
  e.stopPropagation();
  const menu = document.getElementById(`menu-${postId}`);
  menu.style.display = menu.style.display === "none" ? "block" : "none";
  document.addEventListener("click", () => {
    menu.style.display = "none";
  }, { once: true });
}

async function editPost(postId) {
  const postEl = document.querySelector(`[data-post-id="${postId}"]`);
  const captionEl = postEl?.querySelector(".post-caption");
  if (!captionEl) return;

  const currentCaption = captionEl.textContent.replace(captionEl.querySelector(".username").textContent, "").trim();
  const newCaption = prompt("Edit caption:", currentCaption);

  if (newCaption === null || newCaption === currentCaption) return;

  try {
    const res = await fetch(`/api/posts/${postId}`, {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${cachedToken}`,
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ caption: newCaption.trim() })
    });

    if (!res.ok) throw new Error("Failed to update post");

    captionEl.innerHTML = `<span class="username">${cachedUsername}</span> ${renderCaption(newCaption)}`;
    showToast("Post updated successfully", "success");
    document.getElementById(`menu-${postId}`).style.display = "none";
  } catch (e) {
    console.error("Edit error:", e);
    showToast("Failed to update post", "error");
  }
}

async function deletePost(postId) {
  if (!confirm("Are you sure you want to delete this post?")) return;

  try {
    const res = await fetch(`/api/posts/${postId}`, {
      method: "DELETE",
      headers: { Authorization: `Bearer ${cachedToken}` }
    });

    if (!res.ok) throw new Error("Failed to delete post");

    const postEl = document.querySelector(`[data-post-id="${postId}"]`);
    if (postEl) {
      postEl.style.opacity = "0.5";
      setTimeout(() => postEl.remove(), 300);
    }

    showToast("Post deleted successfully", "success");
  } catch (e) {
    console.error("Delete error:", e);
    showToast("Failed to delete post", "error");
  }
}

/* =========================
   FOLLOW
   ========================= */
async function toggleFollow(post, btn) {
  btn.disabled = true;

  const url = post.followingAuthor
    ? `/api/users/${post.authorId}/unfollow`
    : `/api/users/${post.authorId}/follow`;

  const method = post.followingAuthor ? "DELETE" : "POST";

  try {
    const res = await fetch(url, {
      method,
      headers: { Authorization: `Bearer ${cachedToken}` }
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
async function loadComments(postId) {
  const list = document.getElementById(`comments-list-${postId}`);
  if (!list) return;

  try {
    const res = await fetch(`/api/posts/${postId}/comments`, {
      headers: { Authorization: `Bearer ${cachedToken}` }
    });
    if (!res.ok) return;

    const body = await res.json();
    const comments = body.comments || [];

    list.innerHTML = "";
    comments.forEach(c => {
      const div = document.createElement("div");
      div.className = "comment";
      const isOwner = c.user?.id === cachedUserId;

      const deleteBtn = isOwner ? `
        <button onclick="deleteComment(${postId}, ${c.id})" style="background: none; border: none; color: #e74c3c; cursor: pointer; font-size: 12px; margin-left: 8px;">✕</button>
      ` : "";

      div.innerHTML = `
        <span class="username">${c.user?.name || c.user?.username || "user"}</span>
        <span class="comment-text">${escapeHtml(c.content || "")}</span>
        ${deleteBtn}
      `;
      list.appendChild(div);
    });
  } catch (e) {
    console.error("Load comments error:", e);
  }
}

function toggleCommentSection(postId) {
  const el = document.getElementById(`comments-${postId}`);
  if (!el) return;
  el.style.display = el.style.display === "none" ? "block" : "none";
  if (el.style.display === "block") {
    document.getElementById(`comment-input-${postId}`)?.focus();
  }
}

async function addComment(postId) {
  const input = document.getElementById(`comment-input-${postId}`);
  if (!input || !input.value.trim()) return;

  const commentText = input.value.trim();
  input.disabled = true;

  try {
    const res = await fetch(`/api/posts/${postId}/comments`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${cachedToken}`,
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ text: commentText })
    });

    if (!res.ok) throw new Error("Failed to add comment");

    input.value = "";
    loadComments(postId);

    // Update comment count
    const postEl = document.querySelector(`[data-post-id="${postId}"]`);
    const countEl = postEl?.querySelector(".comment-count");
    if (countEl) {
      countEl.textContent = +countEl.textContent + 1;
    }

    showToast("Comment added successfully", "success");
  } catch (e) {
    console.error("Add comment error:", e);
    showToast("Failed to add comment", "error");
  } finally {
    input.disabled = false;
  }
}

async function deleteComment(postId, commentId) {
  if (!confirm("Delete this comment?")) return;

  try {
    const res = await fetch(`/api/posts/${postId}/comments/${commentId}`, {
      method: "DELETE",
      headers: { Authorization: `Bearer ${cachedToken}` }
    });

    if (!res.ok) throw new Error("Failed to delete comment");

    loadComments(postId);

    // Update comment count
    const postEl = document.querySelector(`[data-post-id="${postId}"]`);
    const countEl = postEl?.querySelector(".comment-count");
    if (countEl && +countEl.textContent > 0) {
      countEl.textContent = +countEl.textContent - 1;
    }

    showToast("Comment deleted", "success");
  } catch (e) {
    console.error("Delete comment error:", e);
    showToast("Failed to delete comment", "error");
  }
}

/* =========================
   LIKES
   ========================= */
async function toggleLike(postId, btn) {
  const liked = btn.classList.contains("liked");

  try {
    const res = await fetch(`/api/posts/${postId}/${liked ? "unlike" : "like"}`, {
      method: liked ? "DELETE" : "POST",
      headers: { Authorization: `Bearer ${cachedToken}` }
    });

    if (!res.ok) return;

    btn.classList.toggle("liked");
    const countEl = btn.querySelector(".like-count");
    countEl.textContent = +countEl.textContent + (liked ? -1 : 1);
  } catch (e) {
    console.error("Like error:", e);
  }
}

/* =========================
   CREATE POST
   ========================= */
function handleFileSelect(e) {
  const file = e.target.files[0];
  if (!file) return;

  if (!file.type.startsWith("image/")) {
    showToast("Please select an image", "error");
    return;
  }

  const reader = new FileReader();
  reader.onload = (e) => {
    showPostModal(e.target.result);
  };
  reader.readAsDataURL(file);

  // Reset input
  document.getElementById("sidebarPostInput").value = "";
}

function showPostModal(imageSrc) {
  const existing = document.getElementById("post-modal");
  if (existing) existing.remove();

  const modal = document.createElement("div");
  modal.id = "post-modal";
  modal.style.cssText = `
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0,0,0,0.5);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 2000;
  `;

  const content = document.createElement("div");
  content.style.cssText = `
    background: white;
    border-radius: 12px;
    max-width: 500px;
    width: 90%;
    padding: 0;
    box-shadow: 0 4px 12px rgba(0,0,0,0.15);
    overflow: hidden;
  `;

  content.innerHTML = `
    <div style="background: #f8f8f8; padding: 16px; border-bottom: 1px solid #eee; display: flex; justify-content: space-between; align-items: center;">
      <h2 style="margin: 0; font-size: 18px; font-weight: 600;">Create Post</h2>
      <button id="close-modal" style="background: none; border: none; font-size: 24px; cursor: pointer; color: #999;">×</button>
    </div>

    <div style="padding: 20px;">
      <div style="margin-bottom: 16px;">
        <img src="${imageSrc}" style="width: 100%; border-radius: 8px; max-height: 300px; object-fit: cover;">
      </div>

      <div style="margin-bottom: 16px;">
        <label style="display: block; margin-bottom: 8px; font-weight: 600; font-size: 14px;">Caption</label>
        <textarea
          id="caption-input"
          placeholder="Write a caption... Use #hashtags and @mentions"
          style="
            width: 100%;
            min-height: 80px;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 8px;
            font-family: inherit;
            font-size: 14px;
            resize: vertical;
          "
        ></textarea>
      </div>

      <div style="display: flex; gap: 10px;">
        <button id="cancel-btn" style="
          flex: 1;
          padding: 10px;
          border: 1px solid #ddd;
          background: #f8f8f8;
          border-radius: 8px;
          cursor: pointer;
          font-weight: 600;
          font-size: 14px;
        ">Cancel</button>
        <button id="post-btn" style="
          flex: 1;
          padding: 10px;
          border: none;
          background: #0095f6;
          color: white;
          border-radius: 8px;
          cursor: pointer;
          font-weight: 600;
          font-size: 14px;
        ">Post</button>
      </div>
    </div>
  `;

  modal.appendChild(content);
  document.body.appendChild(modal);

  document.getElementById("close-modal").onclick = () => modal.remove();
  document.getElementById("cancel-btn").onclick = () => modal.remove();
  document.getElementById("post-btn").onclick = async () => {
    const caption = document.getElementById("caption-input").value.trim();
    if (!caption) {
      showToast("Please add a caption", "error");
      return;
    }

    const postBtn = document.getElementById("post-btn");
    postBtn.disabled = true;
    postBtn.textContent = "Posting...";

    try {
      const res = await fetch("/api/posts", {
        method: "POST",
        headers: {
          Authorization: `Bearer ${cachedToken}`,
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          caption: caption,
          imageUrl: imageSrc
        })
      });

      if (!res.ok) throw new Error("Failed to create post");

      showToast("Post created successfully!", "success");
      modal.remove();

      // Reload feed
      setTimeout(() => {
        currentPage = 0;
        loadInitialPosts();
      }, 500);
    } catch (e) {
      console.error("Create post error:", e);
      showToast("Failed to create post", "error");
      postBtn.disabled = false;
      postBtn.textContent = "Post";
    }
  };

  document.getElementById("caption-input").focus();
}

async function loadInitialPosts() {
  const posts = await fetchPosts();
  document.getElementById("feed").innerHTML = "";
  renderFeed(posts);
  await updateUserStats();
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
  const map = { "&": "&amp;", "<": "&lt;", ">": "&gt;" };
  return String(str).replace(/[&<>]/g, m => map[m]);
}

function extractHashtags(text) {
  if (!text) return [];
  return text.match(/#\w+/g) || [];
}
