<script setup>
import { ref, onMounted } from 'vue'

const events = ref([])
const loading = ref(true)
const error = ref('')

async function loadEvents() {
  loading.value = true
  error.value = ''

  try {
    const response = await fetch('/api/events')

    if (!response.ok) {
      throw new Error('Failed to load events')
    }

    events.value = await response.json()
  } catch (err) {
    error.value = err.message
  } finally {
    loading.value = false
  }
}

onMounted(loadEvents)
</script>

<template>
  <main class="container">
    <header>
      <h1>SmartCity Hub</h1>
      <p>Discover events and activities in your community.</p>
    </header>

    <h2>Upcoming Community Events</h2>

    <p v-if="loading">Loading events...</p>

    <p v-else-if="error" class="error">
      {{ error }}
    </p>

    <p v-else-if="events.length === 0">
      No events found.
    </p>

    <section v-else class="event-grid">
      <article
          v-for="event in events"
          :key="event.id"
          class="event-card"
      >
        <span class="category">{{ event.category }}</span>

        <h3>{{ event.title }}</h3>

        <p>{{ event.description }}</p>

        <p><strong>Location:</strong> {{ event.location }}</p>

        <p>
          <strong>Date:</strong>
          {{ new Date(event.eventDate).toLocaleString() }}
        </p>
      </article>
    </section>
  </main>
</template>

<style scoped>
.container {
  max-width: 1100px;
  margin: 0 auto;
  padding: 40px 20px;
  font-family: Arial, sans-serif;
}

header {
  margin-bottom: 40px;
}

h1 {
  color: #2563eb;
  font-size: 36px;
}

.event-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
}

.event-card {
  padding: 24px;
  border: 1px solid #ddd;
  border-radius: 12px;
  background: white;
  color: #222;
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.08);
}

.category {
  display: inline-block;
  padding: 6px 12px;
  background: #dbeafe;
  color: #1d4ed8;
  border-radius: 20px;
  font-size: 13px;
}

.error {
  color: red;
}
</style>