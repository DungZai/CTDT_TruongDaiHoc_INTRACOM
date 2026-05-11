package vn.intracom.chuongtrinhdaotao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.intracom.chuongtrinhdaotao.dto.request.MonHocRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.MonHocResponse;
import vn.intracom.chuongtrinhdaotao.entity.MonHoc;
import vn.intracom.chuongtrinhdaotao.exception.BadRequestException;
import vn.intracom.chuongtrinhdaotao.exception.ResourceNotFoundException;
import vn.intracom.chuongtrinhdaotao.mapper.MonHocMapper;
import vn.intracom.chuongtrinhdaotao.repository.MonHocRepository;
import vn.intracom.chuongtrinhdaotao.repository.MonTienQuyetRepository;
import vn.intracom.chuongtrinhdaotao.service.impl.MonHocServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MonHocServiceTest {

    @Mock private MonHocRepository monHocRepository;
    @Mock private MonTienQuyetRepository monTienQuyetRepository;
    @Mock private MonHocMapper monHocMapper;

    @InjectMocks private MonHocServiceImpl monHocService;

    private MonHoc monHoc;
    private MonHocRequest request;
    private MonHocResponse response;

    @BeforeEach
    void setUp() {
        monHoc = MonHoc.builder()
                .id(1L)
                .maMon("IT001")
                .tenMon("Lập trình Java")
                .tinChi(3)
                .soTietLt(30)
                .soTietTh(15)
                .trangThai(true)
                .build();

        request = new MonHocRequest();
        request.setMaMon("IT001");
        request.setTenMon("Lập trình Java");
        request.setTinChi(3);
        request.setSoTietLt(30);
        request.setSoTietTh(15);
        request.setTrangThai(true);

        response = MonHocResponse.builder()
                .id(1L)
                .maMon("IT001")
                .tenMon("Lập trình Java")
                .tinChi(3)
                .build();
    }

    @Test
    void getAll_shouldReturnListMonHoc() {
        when(monHocRepository.findAll()).thenReturn(List.of(monHoc));
        when(monTienQuyetRepository.findByMonHoc_Id(any())).thenReturn(List.of());
        when(monHocMapper.toResponse(any(), any())).thenReturn(response);

        List<MonHocResponse> result = monHocService.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMaMon()).isEqualTo("IT001");
        verify(monHocRepository).findAll();
    }

    @Test
    void getById_whenExists_shouldReturnMonHoc() {
        when(monHocRepository.findById(1L)).thenReturn(Optional.of(monHoc));
        when(monTienQuyetRepository.findByMonHoc_Id(1L)).thenReturn(List.of());
        when(monHocMapper.toResponse(any(), any())).thenReturn(response);

        MonHocResponse result = monHocService.getById(1L);

        assertThat(result.getMaMon()).isEqualTo("IT001");
    }

    @Test
    void getById_whenNotExists_shouldThrowResourceNotFoundException() {
        when(monHocRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> monHocService.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void create_whenMaMonDuplicate_shouldThrowBadRequestException() {
        when(monHocRepository.existsByMaMon("IT001")).thenReturn(true);

        assertThatThrownBy(() -> monHocService.create(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("IT001");

        verify(monHocRepository, never()).save(any());
    }

    @Test
    void create_whenValid_shouldSaveAndReturnResponse() {
        when(monHocRepository.existsByMaMon("IT001")).thenReturn(false);
        when(monHocMapper.toEntity(request)).thenReturn(monHoc);
        when(monHocRepository.save(monHoc)).thenReturn(monHoc);
        when(monTienQuyetRepository.findByMonHoc_Id(any())).thenReturn(List.of());
        when(monHocMapper.toResponse(any(), any())).thenReturn(response);

        MonHocResponse result = monHocService.create(request);

        assertThat(result.getMaMon()).isEqualTo("IT001");
        verify(monHocRepository).save(monHoc);
    }

    @Test
    void delete_whenExists_shouldDeleteSuccessfully() {
        when(monHocRepository.findById(1L)).thenReturn(Optional.of(monHoc));

        monHocService.delete(1L);

        verify(monHocRepository).delete(monHoc);
    }

    @Test
    void delete_whenNotExists_shouldThrowResourceNotFoundException() {
        when(monHocRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> monHocService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(monHocRepository, never()).delete(any());
    }
}