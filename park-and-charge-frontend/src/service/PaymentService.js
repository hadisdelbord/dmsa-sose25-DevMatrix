import api from './payment_api';

export default {
  createPayment(payment) {
    return api.post('', payment);
  }
};
