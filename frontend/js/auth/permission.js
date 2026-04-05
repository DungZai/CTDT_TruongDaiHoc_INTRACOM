const Permission = {
  isAdmin()     { return TokenService.getRole() === 'ADMIN'; },
  isGiangVien() { return TokenService.getRole() === 'GIANG_VIEN'; },

  can(action) {
    const role = TokenService.getRole();
    const rules = {
      'ADMIN':      ['view','create','update','delete','create_decuong','update_decuong'],
      'GIANG_VIEN': ['view','create_decuong','update_decuong'],
    };
    return (rules[role] || []).includes(action);
  },

  applyUI() {
    const role = TokenService.getRole();

    document.querySelectorAll('[data-role="ADMIN"]').forEach(el => {
      el.style.display = role === 'ADMIN' ? '' : 'none';
    });

    document.querySelectorAll('[data-perm="delete"]').forEach(el => {
      el.style.display = role === 'ADMIN' ? '' : 'none';
    });

    document.querySelectorAll('[data-perm="create"]').forEach(el => {
      el.style.display = role === 'ADMIN' ? '' : 'none';
    });

    document.querySelectorAll('[data-perm="decuong"]').forEach(el => {
      el.style.display = (role === 'ADMIN' || role === 'GIANG_VIEN') ? '' : 'none';
    });
  }
};

document.addEventListener('DOMContentLoaded', () => Permission.applyUI());