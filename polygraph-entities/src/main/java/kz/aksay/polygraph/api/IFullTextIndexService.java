package kz.aksay.polygraph.api;

import kz.aksay.polygraph.entity.FullTextIndex;

public interface IFullTextIndexService extends IGenericService<FullTextIndex, Long> {

	public FullTextIndex findByText(String text);
	
	@Override
	public FullTextIndex save(FullTextIndex fullTextIndex) throws Exception;
}
