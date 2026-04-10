const Toast = {
  _fire(icon, title) {
    Swal.fire({ 
      toast: true, 
      position: 'top-end', 
      icon, 
      title,
      showConfirmButton: false, 
      timer: 3000, 
      timerProgressBar: true,
      customClass: { container: 'toast-on-top' }
    });
  },
  success: msg => Toast._fire('success', msg),
  error:   msg => Toast._fire('error',   msg),
  warning: msg => Toast._fire('warning', msg),
};