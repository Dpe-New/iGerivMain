package it.dpe.igeriv.bo;

import it.dpe.igeriv.vo.BaseVo;

import java.sql.Timestamp;
import java.util.Collection;

import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class BaseServiceImpl implements BaseService {
	private final BaseRepository baseRepository;
	
	@Autowired
	public BaseServiceImpl(@Qualifier("BaseRepository") BaseRepository baseRepository) {
		this.baseRepository = baseRepository;
	}
	
	@Override
	public <T extends BaseVo> T saveBaseVo(T vo) {
		return baseRepository.saveBaseVo(vo);
	}

	@Override
	public <T extends BaseVo> T mergeBaseVo(T vo) {
		return baseRepository.mergeBaseVo(vo);
	}

	@Override
	public <T extends BaseVo> void deleteVo(T vo) {
		baseRepository.deleteVo(vo);
	}

	@Override
	public <T extends BaseVo> void deleteVoList(Collection<T> list) {
		baseRepository.deleteVoList(list);
	}

	@Override
	public <T extends BaseVo> Collection<T> saveVoList(Collection<T> list) {
		return baseRepository.saveVoList(list);
	}

	@Override
	public <T extends BaseVo> Collection<T> mergeVoList(Collection<T> list) {
		return baseRepository.mergeVoList(list);
	}

	@Override
	public Timestamp getSysdate() {
		return baseRepository.getSysdate();
	}

	@Override
	public Long getNextSeqVal(String seqClientiEdicola) {
		return baseRepository.getNextSeqVal(seqClientiEdicola);
	}

	@Override
	public Integer getLastId(Class<?> clazz, String pkName, Criterion restriction) {
		return baseRepository.getLastId(clazz, pkName, restriction);
	}

}
