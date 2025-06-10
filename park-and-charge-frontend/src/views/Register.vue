<template>
  <div class="container d-flex justify-content-center align-items-center" style="min-height: 85vh">
    <div class="card shadow p-4" style="width: 100%; max-width: 500px">
      <h3 class="text-center mb-4">User Registration</h3>

      <form @submit.prevent="registerUser">
        <div class="mb-3">
          <label class="form-label"><strong>Name <span class="text-danger">*</span></strong></label>
          <input v-model="name" type="text" class="form-control" placeholder="Enter your name" required />
        </div>

        <div class="mb-3">
          <label class="form-label"><strong>Email <span class="text-danger">*</span></strong></label>
          <input v-model="email" type="email" class="form-control" placeholder="Enter your email" required />
        </div>

        <div class="mb-3">
          <label class="form-label"><strong>Password <span class="text-danger">*</span></strong></label>
          <input v-model="password" type="password" class="form-control" placeholder="Enter your password" required />
        </div>

        <div class="mb-3">
          <label class="form-label d-block"><strong>Role <span class="text-danger">*</span></strong></label>
          <div class="form-check form-check-inline">
            <input class="form-check-input" type="radio" id="driver" value="DRIVER" v-model="role" />
            <label class="form-check-label" for="driver">Driver</label>
          </div>
          <div class="form-check form-check-inline">
            <input class="form-check-input" type="radio" id="owner" value="OWNER" v-model="role" />
            <label class="form-check-label" for="owner">Owner</label>
          </div>
        </div>

        <div class="d-grid">
          <button type="submit" class="btn btn-success">Register</button>
        </div>
      </form>

      <!-- ✅ Polished Message Display -->
      <div
        v-if="message"
        :class="[
          'alert',
          success ? 'alert-success' : 'alert-danger',
          'text-center',
          'mt-3'
        ]"
      >
        {{ message }}
      </div>

      <div class="text-center mt-3">
        <p class="mb-0">
          Already have an account?
          <a href="/login" class="text-decoration-none">Login</a>
        </p>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      name: '',
      email: '',
      password: '',
      role: 'DRIVER',
      message: '',
      success: false,
    };
  },
  methods: {
    async registerUser() {
      try {
        const response = await axios.post('http://localhost:8082/users/register', {
          name: this.name,
          email: this.email,
          password: this.password,
          role: this.role,
        });
        this.message = 'Registration successful!';
        this.success = true;
      } catch (error) {
        this.message = error.response?.data?.message || 'Registration failed.';
        this.success = false;
      }
    },
  },
};
</script>
