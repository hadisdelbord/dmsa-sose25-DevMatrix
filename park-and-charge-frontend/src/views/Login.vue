<template>
  <div class="container d-flex justify-content-center align-items-center" style="min-height: 80vh">
    <div class="card shadow p-4" style="width: 100%; max-width: 400px">
      <h3 class="text-center mb-4">User Login</h3>

      <form @submit.prevent="loginUser">
        <div class="mb-3">
          <label for="email" class="form-label"><strong>Email</strong></label>
          <input
            v-model="email"
            type="email"
            class="form-control"
            id="email"
            placeholder="Enter your email"
            required
          />
        </div>

        <div class="mb-3">
          <label for="password" class="form-label"><strong>Password</strong></label>
          <input
            v-model="password"
            type="password"
            class="form-control"
            id="password"
            placeholder="Enter your password"
            required
          />
        </div>

        <div class="d-grid">
          <button type="submit" class="btn btn-primary">Login</button>
        </div>
      </form>

      <div v-if="message" class="alert alert-info mt-3 text-center">
        {{ message }}
      </div>

      <div class="text-center mt-3">
        <p class="mb-0">
          Don't have an account?
          <a href="/register" class="text-decoration-none">Register</a>
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
      email: '',
      password: '',
      message: '',
    };
  },
  methods: {
    async loginUser() {
      try {
        const response = await axios.post('http://localhost:8080/users/login', {
          email: this.email,
          password: this.password,
        });
        this.message = `Welcome ${response.data.name}!`;
      } catch (error) {
        this.message = error.response?.data?.message || 'Login failed.';
      }
    },
  },
};
</script>
