package it.dpe.igeriv.bo;

import it.dpe.igeriv.vo.BaseVo;

import java.sql.Timestamp;
import java.util.Collection;

import org.hibernate.criterion.Criterion;

public interface BaseService {
	
	/**
	 * @param <T>
	 * @param vo
	 * @return
	 */
	public <T extends BaseVo> T saveBaseVo(T vo);
	
	/**
	 * @param <T>
	 * @param vo
	 * @return
	 */
	public <T extends BaseVo> T mergeBaseVo(T vo);
	
	/**
	 * @param vo
	 */
	public <T extends BaseVo> void deleteVo(T vo);
	
	/**
	 * @param list
	 */
	public <T extends BaseVo> void deleteVoList(Collection<T> list);
	
	/**
	 * @param list
	 * @return
	 */
	public <T extends BaseVo> Collection<T> saveVoList(Collection<T> list);
	
	/**
	 * @param list
	 * @return
	 */
	public <T extends BaseVo> Collection<T> mergeVoList(Collection<T> list);
	
	/**
	 * @return
	 */
	public Timestamp getSysdate();

	/**
	 * @param seqClientiEdicola
	 * @return
	 */
	public Long getNextSeqVal(String seqClientiEdicola);
	
	/**
	 * @param clazz
	 * @return
	 */
	public Integer getLastId(Class<?> clazz, String pkName, Criterion restriction);
	
}
