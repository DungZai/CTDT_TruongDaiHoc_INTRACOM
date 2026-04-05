const TokenService = {
  KEY: 'jwt_token',

  save(token) {
    sessionStorage.setItem(this.KEY, token);
  },

  get() {
    return sessionStorage.getItem(this.KEY);
  },

  getPayload() {
    try {
      const t = this.get();
      return t ? JSON.parse(atob(t.split('.')[1])) : null;
    } catch { return null; }
  },

  getUsername() {
    return this.getPayload()?.sub || '';
  },

  getRole() {
    const p = this.getPayload();
    return p?.role || p?.roles?.[0] || '';
  },

  isExpired() {
    const p = this.getPayload();
    if (!p?.exp) return true;
    return Date.now() / 1000 > p.exp;
  },

  isValid() {
    return !!this.get() && !this.isExpired();
  },

  clear() {
    sessionStorage.removeItem(this.KEY);
  }
};