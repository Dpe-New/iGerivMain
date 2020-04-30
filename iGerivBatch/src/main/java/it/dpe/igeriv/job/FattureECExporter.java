package it.dpe.igeriv.job;

import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.FileUtils;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.FatturaClienteEdicolaVo;
import it.dpe.igeriv.vo.PagamentoClientiEdicolaVo;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Classe per l'importazione delle immagini delle pubblicazioni iGeriv.
 * La configurazione in integrationContext.xml implementa un monitor sulla cartella di importazione. 
 * 
 * @author romanom
 *
 */
@SuppressWarnings({"unchecked"})
public class FattureECExporter extends QuartzJobBean {
	private String pathEstrattiContoClienti;
	private String pathFatture;
	private BaseDao<?> dao;
	
	public String getPathEstrattiContoClienti() {
		return pathEstrattiContoClienti;
	}

	public void setPathEstrattiContoClienti(String pathEstrattiContoClienti) {
		this.pathEstrattiContoClienti = pathEstrattiContoClienti;
	}

	public String getPathFatture() {
		return pathFatture;
	}

	public void setPathFatture(String pathFatture) {
		this.pathFatture = pathFatture;
	}

	public BaseDao<?> getDao() {
		return dao;
	}

	public void setDao(BaseDao<?> dao) {
		this.dao = dao;
	}

	/**
	 * Esegue l'importazione delle immagini miniature delle pubblicazione inserendole nel DB nella
	 * tabella tbl_9606.  
	 * Il metodo è transazionale.
	 * 
	 * @param File file
	 * @throws IOException
	 * @throws JAXBException
	 */
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		try {
			exportFatture();
			exportEstrattoConto();	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void exportFatture() throws IOException {
		List<FatturaClienteEdicolaVo> list = getAllFatturaClienteEdicolaVo();
		for (FatturaClienteEdicolaVo vo : list) {
			String name = MessageFormat.format(IGerivMessageBundle.get("igeriv.fattura.file.name"), new Object[]{vo.getCliente().getCodCliente().toString(), DateUtilities.getTimestampAsStringExport(new Date(), DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS), vo.getNumeroFattura().toString(), IGerivConstants.FATTURA, DateUtilities.getTimestampAsStringExport(vo.getDataCompetenzaFattura(), DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS)})+ ".pdf";
			File dirEstrattoConto = new File(pathFatture + System.getProperty("file.separator") + "226" + System.getProperty("file.separator") + vo.getCodEdicola());
			if (!dirEstrattoConto.isDirectory()) {
				dirEstrattoConto.mkdirs();
			}
			File outFile = new File(dirEstrattoConto, name);
			InputStream is = new ByteArrayInputStream(vo.getFattura());
			FileUtils.buildFileFromInputStream(is, outFile);
		}
	}

	private List<FatturaClienteEdicolaVo> getAllFatturaClienteEdicolaVo() {
		DetachedCriteria criteria = DetachedCriteria.forClass(FatturaClienteEdicolaVo.class, "pa");
		criteria.createCriteria("pa.cliente", "cli");
		criteria.add(Restrictions.isNotNull("pa.fattura"));
		return dao.findByDetachedCriteria(criteria);
	}

	private void exportEstrattoConto() throws IOException {
		List<PagamentoClientiEdicolaVo> allPagamentoClientiEdicolaVo = getAllPagamentoClientiEdicolaVo();
		for (PagamentoClientiEdicolaVo vo : allPagamentoClientiEdicolaVo) {
			String name = MessageFormat.format(IGerivMessageBundle.get("igeriv.estratto_conto_cliente.file.name"), new Object[]{vo.getCliente().getCodCliente().toString(), DateUtilities.getTimestampAsString(vo.getPk().getDataCompetenzaEstrattoConto(), DateUtilities.FORMATO_DATA)}) + ".xml";
			File xmlFileDir = new File(pathEstrattiContoClienti + System.getProperty("file.separator") + "xml" + System.getProperty("file.separator") + "226" + System.getProperty("file.separator") + vo.getCliente().getCodEdicola());
			if (!xmlFileDir.isDirectory()) {
				xmlFileDir.mkdirs();
			}
			File outFile = new File(xmlFileDir, name);
			InputStream is = new ByteArrayInputStream(vo.getEstrattoContoXml());
			FileUtils.buildFileFromInputStream(is, outFile);
		}
	}
	
	private List<PagamentoClientiEdicolaVo> getAllPagamentoClientiEdicolaVo() {
		HibernateCallback<List<PagamentoClientiEdicolaVo>> action = new HibernateCallback<List<PagamentoClientiEdicolaVo>>() {
			@Override
			public List<PagamentoClientiEdicolaVo> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(PagamentoClientiEdicolaVo.class, "pa");
				criteria.createCriteria("pa.cliente", "cli");
				criteria.add(Restrictions.isNotNull("pa.estrattoContoXml"));
				List<PagamentoClientiEdicolaVo> list = criteria.list();
				for (PagamentoClientiEdicolaVo pe : list) {
					Hibernate.initialize(pe.getEstrattoContoXml());
				}
				return list;
			}
		};
		return dao.findByHibernateCallback(action );
	}

}
