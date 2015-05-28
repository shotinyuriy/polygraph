package kz.aksay.polygraph.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kz.aksay.polygraph.api.IStickerService;
import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.Sticker;

@Service
public class StickerService extends AbstractGenericService<Sticker, Long> implements
		IStickerService {

	private GenericDao<Sticker, Long> stickerDao;

	@Override
	protected GenericDao<Sticker, Long> getDao() {
		return this.stickerDao;
	}
	
	@Autowired
	public void setStickerDao(GenericDao<Sticker, Long> stickerDao) {
		this.stickerDao = stickerDao;
	}

}
