document.addEventListener("DOMContentLoaded", async () => {
  const token = localStorage.getItem("token");

  if (!token) {
    window.location.href = "/login"; 
    return;
  }

  // Fetch posts from backend
  try {
    const response = await fetch("/api/posts", {
      method: "GET",
      headers: {
        "Authorization": `Bearer ${token}`
      }
    });

    if (response.ok) {
      const data = await response.json();
      // server may return { message, data } or { posts: [...] } or the array directly
      const posts = Array.isArray(data.data)
        ? data.data
        : (Array.isArray(data) ? data : (data.posts || data.results || []));
      renderFeed(posts, token);
      setDebug(`Posts: ${posts.length} | token: ${!!token}`);
    } else {
      console.error("Failed to fetch posts");
      showDummyPosts();
    }
  } catch (error) {
    console.error("Error fetching posts:", error);
    showDummyPosts();
  }
});

function setDebug(text) {
  try {
    const el = document.getElementById('debug-banner');
    if (el) el.textContent = text;
  } catch (e) {
    // ignore
  }
}

function renderFeed(posts, token) {
  const feed = document.getElementById("feed");
  feed.innerHTML = ""; // Clear existing posts

  if (posts.length === 0) {
    feed.innerHTML = "<p style='text-align:center; color:#999;'>No posts yet. Follow users to see their posts!</p>";
    return;
  }

  posts.forEach(post => {
    const postElement = document.createElement("div");
    postElement.className = "post";
    postElement.setAttribute('data-post-id', post.id);
    
    // Get timestamp in human-readable format
    const createdAt = new Date(post.createdAt || post.createdAt).toLocaleDateString();

    // support both PostDTO (flat username) and Post (nested user)
    const username = post.user ? (post.user.username || post.user.email) : (post.username || post.userEmail || 'unknown');
    const avatarSeed = (post.user && post.user.id) ? post.user.id : (post.id || 1);
    postElement.innerHTML = `
      <div class="post-header">
        <div class="profile-pic">
          <img src="https://i.pravatar.cc/40?img=${avatarSeed % 50}" alt="profile">
        </div>
        <div>
          <span class="username">${username}</span>
          <button class="follow-btn" id="follow-btn-${post.id}" style="margin-left:8px; font-size:0.8em; display:inline-block; opacity:0.9;">Follow</button>
          <div class="post-time">${createdAt}</div>
        </div>
      </div>
      <div class="post-image">
        <img src="${post.imageUrl || 'https://picsum.photos/600/400?random=' + post.id}" alt="post" ondblclick="toggleLike(${post.id}, this.closest('.post').querySelector('.like-btn'), '${token}')">
      </div>
      <div class="post-actions">
        <button class="action-btn like-btn ${post.likedByCurrentUser ? 'liked' : ''}" onclick="toggleLike(${post.id}, this, '${token}')">
          <span class="heart">❤️</span> <span class="like-count">${(post.likeCount ?? post.likesCount) || 0}</span>
        </button>
        <button class="action-btn" onclick="toggleCommentSection(${post.id})">
          💬 <span class="comment-count">${(post.commentCount ?? post.commentsCount) || 0}</span>
        </button>
      </div>
      <div class="post-caption">
        <span class="username">${username}</span> ${post.caption || post.caption}
      </div>
      <div class="comments-section" id="comments-${post.id}" style="display:none;">
        <div class="comments-list" id="comments-list-${post.id}"></div>
        <div class="add-comment">
          <input type="text" placeholder="Add a comment..." id="comment-input-${post.id}">
          <button onclick="addComment(${post.id}, '${token}')">Post</button>
        </div>
      </div>
    `;
    
    feed.appendChild(postElement);
    loadComments(post.id, token);
    // add follow button for author (fetch userId via search if needed)
    console.debug('setup follow for post', post.id, 'author', post.username || post.userEmail);
    ensureAuthorUserIdAndRenderFollow(post, token).then(() => console.debug('follow setup done', post.id)).catch(e => console.debug('follow setup error', e));
    // attach click on like-count to open likers modal
    const likeCountEl = postElement.querySelector('.like-count');
    if (likeCountEl) {
      likeCountEl.style.cursor = 'pointer';
      likeCountEl.addEventListener('click', (e) => { e.stopPropagation(); showLikers(post.id, token); });
    }
  });
}

function showDummyPosts() {
  const dummyPosts = [
    {
      id: 1,
      user: { username: "john_doe" },
      caption: "Enjoying the sunny day! 🌞",
      imageUrl: "https://picsum.photos/600/400?random=1",
      likesCount: 42,
      commentsCount: 5,
      createdAt: new Date()
    },
    {
      id: 2,
      user: { username: "jane_smith" },
      caption: "Coffee time ☕❤️",
      imageUrl: "https://picsum.photos/600/400?random=2",
      likesCount: 28,
      commentsCount: 3,
      createdAt: new Date()
    }
  ];
  renderFeed(dummyPosts, "");
}

async function toggleLike(postId, button, token) {
  try {
    console.log('toggleLike called for', postId);
    const isCurrentlyLiked = button.classList.contains('liked');
    const url = isCurrentlyLiked ? `/api/posts/${postId}/unlike` : `/api/posts/${postId}/like`;
    const method = isCurrentlyLiked ? 'DELETE' : 'POST';

    const response = await fetch(url, {
      method: method,
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      }
    });

    if (response.ok) {
      // optimistic UI update
      const el = document.querySelector(`[data-post-id="${postId}"] .like-count`);
      const current = el ? parseInt(el.textContent || '0') : 0;
      if (isCurrentlyLiked) {
        button.classList.remove('liked');
        if (el) el.textContent = Math.max(0, current - 1);
        setDebug(`Unliked post ${postId}`);
      } else {
        button.classList.add('liked');
        if (el) el.textContent = current + 1;
        setDebug(`Liked post ${postId}`);
        // animate heart
        const heart = button.querySelector('.heart');
        if (heart) {
          heart.classList.remove('pop');
          void heart.offsetWidth;
          heart.classList.add('pop');
          setTimeout(() => heart.classList.remove('pop'), 400);
        }
      }
      try { updateLikeCount(postId, token); } catch(e){/* best-effort sync */}
    }
  } catch (error) {
    console.error("Error liking post:", error);
  }
}

async function updateLikeCount(postId, token) {
  try {
    // Backend returns either { count, likes } or { likes: [...] } or just an array
    const response = await fetch(`/api/posts/${postId}/likes`, {
      headers: { "Authorization": `Bearer ${token}` }
    });
    if (!response.ok) {
      console.warn('updateLikeCount: non-ok response', response.status);
      return;
    }
    const data = await response.json();
    let count = 0;
    if (typeof data.count === 'number') count = data.count;
    else if (Array.isArray(data.likes)) count = data.likes.length;
    else if (Array.isArray(data)) count = data.length;

    const el = document.querySelector(`[data-post-id="${postId}"] .like-count`);
    if (el) {
      el.textContent = count;
    } else {
      console.warn('like-count element not found for post', postId);
    }
  } catch (error) {
    console.error("Error updating like count:", error);
  }
}

async function checkIfLiked(postId, token) {
  try {
    const response = await fetch(`/api/posts/${postId}/liked-by-user`, {
      headers: { "Authorization": `Bearer ${token}` }
    });
    const data = await response.json();
    if (data.liked) {
      const likeBtn = document.querySelector(`#comments-${postId}`).parentElement.querySelector(".like-btn");
      if (likeBtn) likeBtn.classList.add("liked");
    }
  } catch (error) {
    console.error("Error checking like status:", error);
  }
}

function toggleCommentSection(postId) {
  const section = document.getElementById(`comments-${postId}`);
  section.style.display = section.style.display === "none" ? "block" : "none";
}

async function addComment(postId, token) {
  const input = document.getElementById(`comment-input-${postId}`);
  const text = input.value.trim();
  
  if (!text) return;

  try {
    console.log('addComment called', postId, text);
    const response = await fetch(`/api/posts/${postId}/comments`, {
      method: "POST",
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ text })
    });

    if (response.ok) {
      const data = await response.json();
      console.log('addComment response', data);
      input.value = "";
      loadComments(postId, token);
      setDebug(`Added comment to ${postId}`);
    }
  } catch (error) {
    console.error("Error adding comment:", error);
  }
}

async function loadComments(postId, token) {
  try {
    const response = await fetch(`/api/posts/${postId}/comments`, {
      headers: { "Authorization": `Bearer ${token}` }
    });
    const data = await response.json();
    const commentsList = document.getElementById(`comments-list-${postId}`);
    commentsList.innerHTML = "";

    // comments may be returned as { count, comments } or { data: [...] } or array directly
    const comments = Array.isArray(data.comments) ? data.comments : (Array.isArray(data.data) ? data.data : (Array.isArray(data) ? data : []));
    console.log('loadComments for', postId, comments);
    comments.forEach(comment => {
      const commentEl = document.createElement("div");
      commentEl.className = "comment";
      const authorName = (comment.user && (comment.user.username || comment.user.email)) || (comment.username || comment.userEmail) || 'user';
      const text = comment.content || comment.text || '';
      commentEl.innerHTML = `
        <span class="username">${authorName}</span>
        <span class="comment-text">${escapeHtml(text)}</span>
        <button class="delete-btn" onclick="deleteComment(${postId}, ${comment.id}, '${token}')">✕</button>
      `;
      commentsList.appendChild(commentEl);
    });
    setDebug(`Loaded ${ comments.length } comments for ${postId}`);
  } catch (error) {
    console.error("Error loading comments:", error);
  }
}

async function deleteComment(postId, commentId, token) {
  try {
    const response = await fetch(`/api/posts/${postId}/comments/${commentId}`, {
      method: "DELETE",
      headers: { "Authorization": `Bearer ${token}` }
    });

    if (response.ok) {
      loadComments(postId, token);
    }
  } catch (error) {
    console.error("Error deleting comment:", error);
  }
}

function logout() {
  localStorage.removeItem("token");
  window.location.href = "/login";
}

// Update profile photo (provide an image URL) for current user
async function updateProfilePhoto(imageUrl) {
  const token = localStorage.getItem('token');
  if (!token) { alert('Please login'); return; }
  try {
    const res = await fetch('/api/users/profile/photo', {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` },
      body: JSON.stringify({ imageUrl })
    });
    if (res.ok) {
      setDebug('Profile photo updated');
      setTimeout(() => location.reload(), 500);
    } else if (res.status === 401) {
      alert('Session expired. Please login again.');
      logout();
    } else {
      const txt = await res.text();
      alert('Error updating photo: ' + txt);
    }
  } catch (e) {
    console.error('updateProfilePhoto error', e);
    alert('Error updating profile photo');
  }
}

// Search users by name or email
async function searchUsers(query) {
  const token = localStorage.getItem('token');
  if (!token) return [];
  try {
    const res = await fetch(`/api/users/search?query=${encodeURIComponent(query)}`, {
      headers: { 'Authorization': `Bearer ${token}` }
    });
    if (!res.ok) return [];
    const data = await res.json();
    return data.results || data.data || data || [];
  } catch (e) {
    console.error('searchUsers error', e);
    return [];
  }
}

// expose helpers for quick testing from console
window.updateProfilePhoto = updateProfilePhoto;
window.searchUsers = searchUsers;

async function createPost() {
  const captionEl = document.getElementById('post-caption');
  const imageEl = document.getElementById('post-image');
  const caption = captionEl ? captionEl.value.trim() : '';
  const imageUrl = imageEl ? imageEl.value.trim() : '';
  const token = localStorage.getItem('token');
  if (!token) { alert('Please login'); return; }
  try {
    const res = await fetch('/api/posts', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` },
      body: JSON.stringify({ caption: caption, imageUrl: imageUrl })
    });
    if (res.ok) {
      setDebug('Post created — reloading feed');
      // reload page to fetch fresh posts
      setTimeout(() => location.reload(), 600);
    } else if (res.status === 401) {
      // session expired or invalid token — force logout and prompt to login again
      alert('Session expired. Please login again.');
      logout();
    } else {
      const txt = await res.text();
      alert('Error creating post: ' + txt);
    }
  } catch (e) {
    console.error('createPost error', e);
    alert('Error creating post');
  }
}

// Show likers modal
async function showLikers(postId, token) {
  setDebug(`Loading likers for ${postId}...`);
  try {
    const response = await fetch(`/api/posts/${postId}/likes`, { headers: { 'Authorization': `Bearer ${token}` } });
    if (!response.ok) {
      console.warn('Could not load likers', response.status);
      return;
    }
    const data = await response.json();
    const likers = Array.isArray(data.likes) ? data.likes : (Array.isArray(data) ? data : (Array.isArray(data.data) ? data.data : []));
    const list = document.getElementById('likers-list');
    list.innerHTML = '';
    likers.forEach(l => {
      const div = document.createElement('div');
      div.className = 'liker';
      const name = (l.username || l.email) || (l.user && (l.user.username || l.user.email)) || 'user';
      div.textContent = name;
      list.appendChild(div);
    });
    const backdrop = document.getElementById('likers-backdrop');
    if (backdrop) backdrop.style.display = 'flex';
    setDebug(`Loaded ${ likers.length } likers for ${postId}`);
  } catch (e) {
    console.error('Error loading likers', e);
  }
}

// simple HTML escape for comment text
function escapeHtml(str) {
  return String(str)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#039;');
}

function hideLikersModal() {
  const backdrop = document.getElementById('likers-backdrop');
  if (backdrop) backdrop.style.display = 'none';
}

// --- Follow / Unfollow helpers ---
// cache maps to avoid repeated searches
window._userIdCache = window._userIdCache || {};
window._followingCache = window._followingCache || {};

async function ensureAuthorUserIdAndRenderFollow(post, token) {
  // do not show follow button for posts with no author info
  const authorEmail = post.userEmail || (post.user && post.user.email) || null;
  const authorName = post.username || (post.user && post.user.username) || null;
  if (!authorEmail && !authorName) return;

  // if post belongs to current user (we can compare email stored in token? we don't have it),
  // attempt to hide the follow button if username/email matches current user's profile by calling /api/auth/me is not available,
  // so we'll show button and backend will prevent following yourself.

  const cacheKey = authorEmail || authorName;
  let userId = window._userIdCache[cacheKey];
  if (!userId) {
    // call search endpoint to resolve user id
    try {
      const res = await fetch(`/api/users/search?query=${encodeURIComponent(cacheKey)}`, {
        headers: { 'Authorization': `Bearer ${token}` }
      });
      if (res.ok) {
        const body = await res.json();
        const results = body.results || body.data || body || [];
        // find best match by email or username
        const match = results.find(u => (authorEmail && u.email === authorEmail) || (authorName && u.username === authorName)) || results[0];
        if (match && match.id) {
          userId = match.id;
          window._userIdCache[cacheKey] = userId;
        }
      }
    } catch (e) {
      console.debug('user search failed', e);
    }
  }

  const btn = document.getElementById(`follow-btn-${post.id}`);
  if (!btn) return;
  if (!userId) {
    // cannot resolve id, hide button
    btn.style.display = 'none';
    return;
  }

  btn.style.display = 'inline-block';
  btn.disabled = true;

  // check following state
  try {
    const r = await fetch(`/api/users/${userId}/is-following`, { headers: { 'Authorization': `Bearer ${token}` } });
    if (r.ok) {
      const json = await r.json();
      const isFollowing = json.following === true || json.following === true;
      window._followingCache[userId] = isFollowing;
      renderFollowButtonState(btn, isFollowing);
    } else {
      renderFollowButtonState(btn, false);
    }
  } catch (e) {
    console.debug('is-following check failed', e);
    renderFollowButtonState(btn, false);
  } finally {
    btn.disabled = false;
  }

  btn.onclick = async (ev) => {
    ev.stopPropagation();
    btn.disabled = true;
    const currentlyFollowing = !!window._followingCache[userId];
    try {
      if (currentlyFollowing) {
        const r = await fetch(`/api/users/${userId}/unfollow`, { method: 'DELETE', headers: { 'Authorization': `Bearer ${token}` } });
        if (r.ok) {
          window._followingCache[userId] = false;
          renderFollowButtonState(btn, false);
          setDebug(`Unfollowed user ${userId}`);
        } else {
          const txt = await r.text();
          console.warn('unfollow failed', r.status, txt);
          alert('Could not unfollow: ' + txt);
        }
      } else {
        const r = await fetch(`/api/users/${userId}/follow`, { method: 'POST', headers: { 'Authorization': `Bearer ${token}` } });
        if (r.ok) {
          window._followingCache[userId] = true;
          renderFollowButtonState(btn, true);
          setDebug(`Followed user ${userId}`);
        } else if (r.status === 400) {
          const txt = await r.text();
          alert('Could not follow: ' + txt);
        } else if (r.status === 401) {
          alert('Session expired. Please login again.');
          logout();
        } else {
          const txt = await r.text();
          alert('Could not follow: ' + txt);
        }
      }
    } catch (e) {
      console.error('follow toggle error', e);
    } finally {
      btn.disabled = false;
    }
  };
}

function renderFollowButtonState(btn, isFollowing) {
  btn.textContent = isFollowing ? 'Following' : 'Follow';
  btn.classList.toggle('following', !!isFollowing);
}

window.ensureAuthorUserIdAndRenderFollow = ensureAuthorUserIdAndRenderFollow;
