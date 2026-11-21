<script setup>
import { ref, onMounted } from 'vue'
import ServiceRequests from './components/ServiceRequests.vue'

const currentPage = ref('events')
const events = ref([])
const loading = ref(false)
const error = ref('')
const selectedCategory = ref('')
const selectedDate = ref('')
const searchKeyword = ref('')
const selectedLocation = ref('')
const categories = [
  'Community Service',
  'Public Safety',
  'Cultural'
]

// Ignore stale responses when users change filters before a request finishes.
let requestController = null

async function loadEvents() {
  if (requestController) requestController.abort()
  requestController = new AbortController()
  const controller = requestController

  loading.value = true
  error.value = ''

  try {
    const params = new URLSearchParams()

    if (selectedCategory.value) params.append('category', selectedCategory.value)
    if (selectedDate.value) params.append('date', selectedDate.value)
    if (searchKeyword.value.trim()) params.append('keyword', searchKeyword.value.trim())
    if (selectedLocation.value.trim()) {
      params.append('location', selectedLocation.value.trim())
    }

    const query = params.toString()
    const url = query ? `/api/events?${query}` : '/api/events'
    const response = await fetch(url, { signal: controller.signal })

    if (!response.ok) {
      let message = 'Unable to load events. Please try again.'
      try {
        const details = await response.json()
        if (details?.error) message = details.error
      } catch {
      }
      throw new Error(message)
    }

    events.value = await response.json()
  } catch (err) {
    if (err.name !== 'AbortError') {
      events.value = []
      error.value = err.message === 'Failed to fetch'
        ? 'Cannot connect to SmartCity Hub. Check that the backend is running.'
        : err.message
    }
  } finally {
    if (requestController === controller) {
      loading.value = false
      requestController = null
    }
  }
}

function clearFilters() {
  selectedCategory.value = ''
  selectedDate.value = ''
  searchKeyword.value = ''
  selectedLocation.value = ''
  loadEvents()
}

onMounted(loadEvents)
</script>

<template>
  <main class="container">
    <header>
      <h1>SmartCity Hub</h1>
      <p>Discover events and activities in your community.</p>
    </header>

    <nav class="main-nav">
      <button @click="currentPage = 'events'">
        Community Events
      </button>
      <button @click="currentPage = 'services'">
        City Services
      </button>
    </nav>

    <ServiceRequests v-if="currentPage === 'services'" />

    <div v-if="currentPage === 'events'">

    <h2>Upcoming Community Events</h2>

    <form class="filters" @submit.prevent="loadEvents">
      <div class="filter-group">
        <label for="keyword">Keyword Search</label>
        <input id="keyword" v-model="searchKeyword" type="search"
               placeholder="Search titles or descriptions..." />
      </div>

      <div class="filter-group">
        <label for="category">Category</label>
        <select id="category" v-model="selectedCategory" @change="loadEvents">
          <option value="">All Categories</option>
          <option v-for="category in categories" :key="category" :value="category">
            {{ category }}
          </option>
        </select>
      </div>

      <div class="filter-group">
        <label for="eventDate">Event Date</label>
        <input id="eventDate" v-model="selectedDate" type="date" @change="loadEvents" />
      </div>

      <div class="filter-group">
        <label for="location">Location</label>
        <input
            id="location"
            v-model="selectedLocation"
            type="search"
            placeholder="Search by location..."
        />
      </div>

      <button class="search-button" type="submit">Search</button>
      <button class="clear-button" type="button" @click="clearFilters">Clear Filters</button>
    </form>

    <p v-if="loading" class="status" role="status">Loading events...</p>

    <div v-else-if="error" class="error" role="alert">
      <p>{{ error }}</p>
      <button type="button" class="search-button" @click="loadEvents">Try Again</button>
    </div>

    <div v-else-if="events.length === 0" class="empty-state" role="status">
      <h3>No matching events found</h3>
      <p>Try a different keyword, date, or category.</p>
      <button type="button" class="search-button" @click="clearFilters">Clear Filters</button>
    </div>

    <section v-else class="event-grid">
      <article v-for="event in events" :key="event.id" class="event-card">
        <span class="category">{{ event.category }}</span>
        <h3>{{ event.title }}</h3>
        <p>{{ event.description }}</p>
        <p><strong>Location:</strong> {{ event.location }}</p>
        <p><strong>Date:</strong> {{ new Date(event.eventDate).toLocaleString() }}</p>
      </article>
    </section>
    </div>
  </main>
</template>

<style scoped>
.container {
  max-width: 1100px;
  margin: 0 auto;
  padding: 40px 20px;
  font-family: Arial, sans-serif;
}

header { margin-bottom: 40px; }

h1 { color: #2563eb; font-size: 36px; }

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

.filters {
  display: flex;
  flex-wrap: wrap;
  align-items: end;
  gap: 20px;
  padding: 20px;
  margin-bottom: 30px;
  background: #eef4ff;
  border-radius: 12px;
}

.filter-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.filter-group label {
  font-size: 14px;
  font-weight: bold;
  color: #374151;
}

.filter-group select,
.filter-group input {
  padding: 10px 12px;
  border: 1px solid #cbd5e1;
  border-radius: 8px;
  background: white;
  color: #1f2937;
  font-size: 14px;
}

.search-button,
.clear-button {
  padding: 11px 18px;
  border: none;
  border-radius: 8px;
  background: #2563eb;
  color: white;
  font-weight: bold;
  cursor: pointer;
}

.clear-button { background: #475569; }

.search-button:hover { background: #1d4ed8; }
.clear-button:hover { background: #334155; }

.error {
  padding: 20px;
  border-radius: 8px;
  background: #fef2f2;
  color: #991b1b;
}

.status, .empty-state {
  padding: 24px;
  border-radius: 8px;
  background: #f8fafc;
  color: #334155;
}

.main-nav {
  display: flex;
  gap: 12px;
  margin-bottom: 30px;
}

.main-nav button {
  padding: 12px 20px;
  border: none;
  border-radius: 8px;
  background: #1e40af;
  color: white;
  font-weight: bold;
  cursor: pointer;
}
</style>
