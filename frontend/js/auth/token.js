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
      if (!t) return null;

      // Decode base64url → UTF-8 đúng cách (hỗ trợ tiếng Việt)
      const base64 = t.split('.')[1]
        .replace(/-/g, '+')
        .replace(/_/g, '/');
      const json = decodeURIComponent(
        atob(base64).split('').map(c =>
          '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
        ).join('')
      );
      return JSON.parse(json);
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