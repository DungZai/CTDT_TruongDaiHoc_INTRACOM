package vn.intracom.chuongtrinhdaotao.dto.response;
 
import lombok.Data;
import org.springframework.data.domain.Page;
 
import java.util.List;
 
@Data
public class PageResponse<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
 
    public static <T> PageResponse<T> of(Page<T> pageData) {
        PageResponse<T> res = new PageResponse<>();
        res.setContent(pageData.getContent());
        res.setPage(pageData.getNumber());
        res.setSize(pageData.getSize());
        res.setTotalElements(pageData.getTotalElements());
        res.setTotalPages(pageData.getTotalPages());
        res.setFirst(pageData.isFirst());
        res.setLast(pageData.isLast());
        return res;
    }
}