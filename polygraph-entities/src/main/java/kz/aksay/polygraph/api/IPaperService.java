package kz.aksay.polygraph.api;

import java.util.List;
import java.util.Set;

import kz.aksay.polygraph.entity.Format;
import kz.aksay.polygraph.entity.Paper;
import kz.aksay.polygraph.entity.PaperType;

public interface IPaperService extends IGenericService<Paper, Long> {

	public int deleteAllByPaperType(PaperType paperType);

	public List<Paper> findByExampleAndPaperType(Paper example,
			PaperType paperType);
	
	public Set<Format> findAvailableFormats();

}
