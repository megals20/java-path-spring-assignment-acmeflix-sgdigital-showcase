package com.acmeflix.scheduler;

		import com.acmeflix.base.BaseComponent;
		import org.springframework.beans.factory.annotation.Value;
		import org.springframework.context.annotation.Configuration;
		import org.springframework.scheduling.annotation.EnableScheduling;
		import org.springframework.scheduling.annotation.Scheduled;
		import org.springframework.transaction.annotation.Transactional;

		import javax.persistence.EntityManager;
		import javax.persistence.PersistenceContext;
		import java.io.File;
		import java.text.SimpleDateFormat;
		import java.util.Date;

@Configuration
@EnableScheduling
@Transactional(readOnly = true)
public class DBAutoBackupScheduler extends BaseComponent {
	@PersistenceContext
	private EntityManager entityManager;

	@Value("${spring.datasource.hikari.backup-location}")
	private String backupLocation;

	@Scheduled(cron = "0 10 * * * ?") // For every 10 mins
	public void backup() {

		logger.info("Backup Started at " + new Date());
		Date backupDate = new Date();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy-hh-mm");
		String backupDateStr = format.format(backupDate);

		String fileName = "Daily_DB_Backup"; // default file name
		File f1 = new File(backupLocation);
		f1.mkdir(); // create folder if not exist

		String saveFileName = fileName + "_" + backupDateStr + ".zip";
		String savePath = backupLocation + File.separator + saveFileName;

		entityManager.createNativeQuery("BACKUP TO '" + savePath + "'").executeUpdate();
		logger.info("Backup Completed at " + new Date());
	}
}

