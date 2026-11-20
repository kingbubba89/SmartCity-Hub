
<script setup>
import { ref, onMounted } from 'vue'

const activeTab = ref('submit')
const requests = ref([])
const selectedRequest = ref(null)
const trackingId = ref('')
const loading = ref(false)
const message = ref('')
const error = ref('')

const form = ref({
  residentName: '',
  title: '',
  description: '',
  location: ''
})

async function api(url, options = {}) {
  const response = await fetch(url, options)

  if (!response.ok) {
    let details = {}
    try {
      details = await response.json()
    } catch {
      // Use the fallback error.
    }
    throw new Error(details.error || details.message ||
        `Request failed (${response.status})`)
  }

  return response.json()
}

async function submitRequest() {
  loading.value = true
  error.value = ''
  message.value = ''

  try {
    const created = await api('/api/requests', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form.value)
    })

    message.value = `Request #${created.id} submitted successfully!`
    trackingId.value = String(created.id)
    selectedRequest.value = created

    form.value = {
      residentName: '',
      title: '',
      description: '',
      location: ''
    }
  } catch (err) {
    error.value = err.message
  } finally {
    loading.value = false
  }
}

async function trackRequest() {
  if (!trackingId.value.trim()) return

  loading.value = true
  error.value = ''
  message.value = ''
  selectedRequest.value = null

  try {
    selectedRequest.value = await api(
        `/api/requests/${encodeURIComponent(trackingId.value.trim())}`
    )
  } catch (err) {
    error.value = err.message
  } finally {
    loading.value = false
  }
}

async function loadRequests() {
  loading.value = true
  error.value = ''

  try {
    requests.value = await api('/api/requests')
  } catch (err) {
    error.value = err.message
  } finally {
    loading.value = false
  }
}

async function updateRequest(request) {
  error.value = ''
  message.value = ''

  try {
    const updated = await api(`/api/requests/${request.id}`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        status: request.status,
        department: request.department,
        progressNotes: request.progressNotes
      })
    })

    message.value = `Request #${updated.id} updated successfully.`
    await loadRequests()
  } catch (err) {
    error.value = err.message
  }
}

ffunction changeTab(tab) {
  activeTab.value = tab
  error.value = ''
  message.value = ''

  if (tab === 'admin') loadRequests()
  if (tab === 'track' && trackingId.value) trackRequest()
}

onMounted(() => {})
</script>

<template>
  <div class="service-container">
    <h2>City Service Requests</h2>
    <p>Report issues, track progress, and manage city services.</p>

    <nav class="tabs">
      <button
          :class="{ active: activeTab === 'submit' }"
          @click="changeTab('submit')">
        Submit Request
      </button>

      <button
          :class="{ active: activeTab === 'track' }"
          @click="changeTab('track')">
        Track Request
      </button>

      <button
          :class="{ active: activeTab === 'admin' }"
          @click="changeTab('admin')">
        Admin Dashboard
      </button>
    </nav>

    <p v-if="message" class="success">{{ message }}</p>
    <p v-if="error" class="error" role="alert">{{ error }}</p>

    <!-- Resident submission -->
    <section v-if="activeTab === 'submit'" class="panel">
      <h3>Report a City Issue</h3>

      <form @submit.prevent="submitRequest" class="request-form">
        <label>
          Your Name
          <input v-model="form.residentName" required />
        </label>

        <label>
          Issue Title
          <input v-model="form.title"
                 placeholder="Broken streetlight" required />
        </label>

        <label>
          Description
          <textarea v-model="form.description"
                    rows="4" required />
        </label>

        <label>
          Location
          <input v-model="form.location"
                 placeholder="123 Main Street" required />
        </label>

        <button type="submit" :disabled="loading">
          {{ loading ? 'Submitting...' : 'Submit Request' }}
        </button>
      </form>

      <div v-if="selectedRequest" class="result">
        <strong>Request ID: #{{ selectedRequest.id }}</strong>
        <p>Save this number to track your request.</p>
        <button @click="changeTab('track')">
          Track This Request
        </button>
      </div>
    </section>

    <!-- Resident tracking -->
    <section v-if="activeTab === 'track'" class="panel">
      <h3>Track Your Request</h3>

      <form @submit.prevent="trackRequest" class="tracking-form">
        <input v-model="trackingId" type="number" min="1"
               placeholder="Enter request ID" required />
        <button type="submit" :disabled="loading">
          {{ loading ? 'Searching...' : 'Track' }}
        </button>
      </form>

      <article v-if="selectedRequest" class="result">
        <h3>{{ selectedRequest.title }}</h3>
        <p><strong>ID:</strong> #{{ selectedRequest.id }}</p>
        <p><strong>Status:</strong>
          <span class="status">{{ selectedRequest.status }}</span>
        </p>
        <p><strong>Department:</strong>
          {{ selectedRequest.department }}
        </p>
        <p><strong>Location:</strong>
          {{ selectedRequest.location }}
        </p>
        <p><strong>Description:</strong>
          {{ selectedRequest.description }}
        </p>
        <p><strong>Progress:</strong>
          {{ selectedRequest.progressNotes }}
        </p>
        <p><strong>Last Updated:</strong>
          {{ selectedRequest.updatedAt
              ? new Date(selectedRequest.updatedAt).toLocaleString()
              : 'Not available' }}
        </p>
      </article>
    </section>

    <!-- Demonstration administration interface -->
    <section v-if="activeTab === 'admin'" class="panel">
      <h3>City Administration Dashboard</h3>

      <div class="stats">
        <div>
          <strong>{{ requests.length }}</strong>
          <span>Total Requests</span>
        </div>
        <div>
          <strong>
            {{ requests.filter(r => r.status === 'SUBMITTED').length }}
          </strong>
          <span>Submitted</span>
        </div>
        <div>
          <strong>
            {{ requests.filter(r => r.status === 'IN_PROGRESS').length }}
          </strong>
          <span>In Progress</span>
        </div>
        <div>
          <strong>
            {{ requests.filter(r => r.status === 'RESOLVED').length }}
          </strong>
          <span>Resolved</span>
        </div>
      </div>

      <button @click="loadRequests" :disabled="loading">
        Refresh Requests
      </button>

      <p v-if="loading">Loading requests...</p>

      <p v-else-if="requests.length === 0">
        No service requests have been submitted.
      </p>

      <div v-for="request in requests" :key="request.id"
           class="request-card">
        <h4>#{{ request.id }} — {{ request.title }}</h4>
        <p>{{ request.description }}</p>
        <p><strong>Resident:</strong> {{ request.residentName }}</p>
        <p><strong>Location:</strong> {{ request.location }}</p>

        <label>
          Department
          <select v-model="request.department">
            <option>Unassigned</option>
            <option>Public Works</option>
            <option>Road Maintenance</option>
            <option>Water Department</option>
            <option>Parks and Recreation</option>
            <option>Public Safety</option>
          </select>
        </label>

        <label>
          Status
          <select v-model="request.status">
            <option value="SUBMITTED">Submitted</option>
            <option value="IN_PROGRESS">In Progress</option>
            <option value="RESOLVED">Resolved</option>
          </select>
        </label>

        <label>
          Progress Notes
          <textarea v-model="request.progressNotes"
                    rows="2" />
        </label>

        <button @click="updateRequest(request)">
          Save Changes
        </button>
      </div>
    </section>
  </div>
</template>

<style scoped>
.service-container {
  max-width: 1100px;
  margin: 0 auto;
  color: #1f2937;
}

.tabs {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin: 25px 0;
}

button {
  padding: 11px 18px;
  border: none;
  border-radius: 8px;
  background: #2563eb;
  color: white;
  font-weight: bold;
  cursor: pointer;
}

button:disabled {
  opacity: 0.6;
  cursor: wait;
}

.tabs button {
  background: #475569;
}

.tabs button.active {
  background: #2563eb;
}

.panel {
  padding: 24px;
  background: #f8fafc;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
}

.request-form,
.request-card {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

label {
  display: flex;
  flex-direction: column;
  gap: 7px;
  font-weight: 600;
}

input, textarea, select {
  box-sizing: border-box;
  width: 100%;
  padding: 11px;
  border: 1px solid #cbd5e1;
  border-radius: 7px;
  font: inherit;
  background: white;
  color: #1f2937;
}

.tracking-form {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}

.result, .request-card {
  padding: 20px;
  margin-top: 20px;
  background: white;
  border: 1px solid #dbe2ea;
  border-radius: 10px;
}

.status {
  font-weight: bold;
  color: #2563eb;
}

.stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(140px, 1fr));
  gap: 15px;
  margin: 20px 0;
}

.stats div {
  padding: 20px;
  background: white;
  border-radius: 10px;
  border: 1px solid #e2e8f0;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.stats strong {
  font-size: 28px;
  color: #2563eb;
}

.stats span {
  font-size: 13px;
}

.success {
  padding: 15px;
  background: #dcfce7;
  color: #166534;
  border-radius: 8px;
}

.error {
  padding: 15px;
  background: #fee2e2;
  color: #991b1b;
  border-radius: 8px;
}
</style>
