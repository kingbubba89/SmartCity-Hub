
<script setup>
import { ref, onMounted } from 'vue'

const events = ref([])
const loading = ref(true)
const error = ref('')
const selectedCategory = ref('')
const selectedDate = ref('')

const categories = [
  'Community Service',
  'Public Safety',
  'Cultural'
]

async function loadEvents() {
  loading.value = true
  error.value = ''

  try {
    const params = new URLSearchParams()

    if (selectedCategory.value) {
      params.append('category', selectedCategory.value)
    }

    if (selectedDate.value) {
      params.append('date', selectedDate.value)
    }

    const query = params.toString()
    const url = query ? `/api/events?${query}` : '/api/events'
    const response = await fetch(url)

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

function clearFilters() {
  selectedCategory.value = ''
  selectedDate.value = ''
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

    <h2>Upcoming Community Events</h2>

    <section class="filters">
      <div class="filter-group">
        <label for="category">Category</label>

        <select
            id="category"
            v-model="selectedCategory"
            @change="loadEvents"
        >
          <option value="">All Categories</option>

          <option
              v-for="category in categories"
              :key="category"
              :value="category"
          >
            {{ category }}
          </option>
        </select>
      </div>

      <div class="filter-group">
        <label for="eventDate">Event Date</label>

        <input
            id="eventDate"
            type="date"
            v-model="selectedDate"
            @change="loadEvents"
        />
      </div>

      <button class="clear-button" @click="clearFilters">
        Clear Filters
      </button>
    </section>


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

.clear-button {
  padding: 11px 18px;
  border: none;
  border-radius: 8px;
  background: #2563eb;
  color: white;
  font-weight: bold;
  cursor: pointer;
}

.clear-button:hover {
  background: #1d4ed8;
}

</style>