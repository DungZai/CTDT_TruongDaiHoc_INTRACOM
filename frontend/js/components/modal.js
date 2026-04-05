const Modal = {
  open(id)  { const el = document.getElementById(id); el && new bootstrap.Modal(el).show(); },
  close(id) { const el = document.getElementById(id); el && bootstrap.Modal.getInstance(el)?.hide(); },
  reset(id) {
    document.getElementById(id)
      ?.querySelectorAll('input,textarea,select')
      .forEach(el => el.value = '');
  },
};