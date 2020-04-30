package it.dpe.inforiv.ftp.filter;

import it.dpe.igeriv.bo.batch.IGerivBatchService;
import it.dpe.igeriv.util.IGerivBatchConstants;
import it.dpe.igeriv.vo.InforivFtpFileVo;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("InforivFTPFileFilter")
@Scope("prototype")
public class InforivFTPFileFilter implements FTPFileFilter {
	public static final Pattern patternFileName = Pattern.compile(IGerivBatchConstants.FILE_INFORIV_INPUT_PATTERN, Pattern.CASE_INSENSITIVE);
	private File dir;
	@Autowired
	private IGerivBatchService iGerivBatchService;
	
	public InforivFTPFileFilter() {}
	
	public InforivFTPFileFilter(File dir) {
		this.dir = dir;
	}
	
	@Override
	public boolean accept(FTPFile file) {
		String fileName = file.getName();
		Matcher matcher = patternFileName.matcher(fileName);
		InforivFtpFileVo infFile = iGerivBatchService.getInforivFtpFile(fileName);
		if (matcher.matches() && !new File(dir, fileName).exists() && infFile == null) {
			return true;
		}
		return false;
	}
	
	public IGerivBatchService getiGerivBatchService() {
		return iGerivBatchService;
	}

	public void setiGerivBatchService(IGerivBatchService iGerivBatchService) {
		this.iGerivBatchService = iGerivBatchService;
	}

}
