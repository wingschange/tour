<template>
  <div style="max-width: 600px; margin: 2rem auto; font-family: sans-serif;">
    <h1>Posts</h1>

    <p v-if="loading">Loading...</p>

    <p v-else-if="error" style="color: red;">{{ error }}</p>

    <ul v-else-if="posts.length">
      <li v-for="post in posts" :key="post.id">{{ post.title }}</li>
    </ul>

    <p v-else>No posts found.</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const posts = ref([])
const loading = ref(true)
const error = ref(null)

onMounted(async () => {
  try {
    const response = await axios.get(`${import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'}/posts`)
    posts.value = response.data
  } catch (err) {
    error.value = 'Failed to load posts: ' + (err.message || 'Unknown error')
  } finally {
    loading.value = false
  }
})
</script>
