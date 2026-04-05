const Toast = {
  _fire(icon, title) {
    Swal.fire({ toast: true, position: 'top-end', icon, title,
      showConfirmButton: false, timer: 2500, timerProgressBar: true });
  },
  success: msg => Toast._fire('success', msg),
  error:   msg => Toast._fire('error',   msg),
  warning: msg => Toast._fire('warning', msg),
};