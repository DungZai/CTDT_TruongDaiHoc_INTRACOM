const Validator = {
  required(v, label) {
    if (!v || String(v).trim() === '') {
      Toast.warning(`Vui lòng nhập ${label}.`);
      return false;
    }
    return true;
  },
  positiveInt(v, label) {
    if (!Number.isInteger(Number(v)) || Number(v) <= 0) {
      Toast.warning(`${label} phải là số nguyên dương.`);
      return false;
    }
    return true;
  },
  form(fields = {}) {
    for (const [label, val] of Object.values(fields)) {
      if (!this.required(val, label)) return false;
    }
    return true;
  },
};