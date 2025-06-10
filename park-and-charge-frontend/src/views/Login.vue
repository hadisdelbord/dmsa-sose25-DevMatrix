<template>
  <div class="container d-flex justify-content-center align-items-center" style="min-height: 85vh">
    <div class="card shadow p-4" style="width: 100%; max-width: 500px">
      <h3 class="text-center mb-4">User Login</h3>

      <form @submit.prevent="loginUser">
        <div class="mb-3">
          <label class="form-label"><strong>Email <span class="text-danger">*</span></strong></label>
          <input v-model="email" type="email" class="form-control" placeholder="Enter your email" required />
        </div>

        <div class="mb-3">
          <label class="form-label"><strong>Password <span class="text-danger">*</span></strong></label>
          <input v-model="password" type="password" class="form-control" placeholder="Enter your password" required />
        </div>

        <div class="d-grid">
          <button type="submit" class="btn btn-primary">Login</button>
        </div>
      </form>

      <div v-if="message" class="alert alert-info text-center mt-3">
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
      message: ''
    };
  },
  methods: {
    async loginUser() {
      try {
        const response = await axios.post('http://localhost:8082/users/login', {
          email: this.email,
          password: this.password
        });

        const { token, role } = response.data;

        // Store token in localStorage
        localStorage.setItem('token', token);
        localStorage.setItem('role', role);

        this.message = 'Login successful!';

        // Redirect based on role
        if (role === 'DRIVER') {
          this.$router.push('/driver');  // Change if your route is different
        } else if (role === 'OWNER') {
          this.$router.push('/owner'); // Placeholder for owner page
        }
      } catch (error) {
        this.message = error.response?.data || 'Login failed. Check your credentials.';
      }
    }
  }
};
</script>
