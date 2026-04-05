const Formatter = {
  date(v) {
    return v ? new Date(v).toLocaleDateString('vi-VN') : '—';
  },
  trangThai(v) {
    return v
      ? '<span class="badge" style="background:#dcfce7;color:#15803d">Hoạt động</span>'
      : '<span class="badge" style="background:#f1f5f9;color:#64748b">Ẩn</span>';
  },
};