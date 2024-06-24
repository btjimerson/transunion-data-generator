package com.yugabyte.tu;

import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.github.javafaker.Faker;

import jakarta.transaction.Transactional;
import lombok.extern.apachecommons.CommonsLog;

@SpringBootApplication
@CommonsLog
public class TransunionDataGeneratorApplication implements ApplicationRunner {

	@Autowired
	AccountHistoryRepository accountHistoryRepository;

	// default number of records to insert
	private static final Long NUMBER_OF_RECORDS = 4000L;

	// how many times to update all of the records
	private static final Long NUMBER_OF_UPDATES = 1L;

	public static void main(String[] args) {
		SpringApplication.run(TransunionDataGeneratorApplication.class, args);
	}

	@Override
	@Transactional
	public void run(ApplicationArguments args) throws Exception {
		List<String> arguments = args.getNonOptionArgs();
		Faker faker = new Faker();

		if (arguments.size() <= 0) {
			printUsage();
			System.exit(0);
		}

		String action = arguments.get(0);
		if ("insert".equalsIgnoreCase(action)) {

			// how many records to insert
			Long numberOfRecords;
			if (arguments.size() > 1 && NumberUtils.isCreatable(arguments.get(1))) {
				numberOfRecords = Long.parseLong(arguments.get(1));
			} else {
				numberOfRecords = NUMBER_OF_RECORDS;
			}

			// clean up
			accountHistoryRepository.deleteAll();

			// random numbers
			Random pkRandom = new Random();
			Random skRandom = new Random();
			Random recordAcctIdRandom = new Random();
			Random recordPartyIdRandom = new Random();

			// insert test data
			for (int i = 0; i < numberOfRecords; i++) {

				AccountHistory accountHistory = new AccountHistory();

				Record newRecord = new Record();
				newRecord.setAcctId(recordAcctIdRandom.nextLong());
				newRecord.setPartyId(recordPartyIdRandom.nextLong());

				accountHistory.setPk(String.valueOf(pkRandom.nextLong()));
				accountHistory.setEntityGroup("acct");
				accountHistory.setEntity("hist_inctv");
				accountHistory.setSk(String.valueOf(skRandom.nextLong()) + "#");
				accountHistory.setRecord(newRecord);
				accountHistory.setRecords(faker.chuckNorris().fact().getBytes());
				accountHistory.setUpdVersionId(1L);
				accountHistory.setUpdTsp(new Timestamp(System.currentTimeMillis()));
				accountHistory.setUserSrcId("2BF1018002");
				log.info(String.format("Inserting account history [%s]", accountHistory));
				accountHistoryRepository.save(accountHistory);
			}

			log.info(String.format("Inserted %d records.", numberOfRecords));

		} else if ("update".equalsIgnoreCase(action)) {

			// number of times to update all records
			Long numberOfUpdates;
			if (arguments.size() > 1 && NumberUtils.isCreatable(arguments.get(1))) {
				numberOfUpdates = Long.valueOf(arguments.get(1));
			} else {
				numberOfUpdates = NUMBER_OF_UPDATES;
			}

			Long counter = 0L;
			for (int i = 0; i < numberOfUpdates; i++) {
				// get existing records
				List<AccountHistory> allHistories = accountHistoryRepository.findAll();

				// update records
				for (AccountHistory history : allHistories) {

					String oldAccountHistory = history.toString();
					history.setRecords(faker.harryPotter().quote().getBytes());
					history.setUpdTsp(new Timestamp(System.currentTimeMillis()));
					String newAccountHistory = history.toString();
					log.info(String.format("Updating account history [%s] to [%s]", oldAccountHistory,
							newAccountHistory));
					accountHistoryRepository.save(history);
					counter++;
				}
			}

			log.info(String.format("Updated %d records", counter));

		} else {
			printUsage();
			System.exit(0);
		}
	}

	private void printUsage() {
		System.out.println("**************************");
		System.out.println(
				"Usage: java -jar  transunion-data-generator*.jar <insert | update> <number of records | number of updates>");
		System.out.println("Example: java -jar transunion-data-generator*.jar insert 5000000");
		System.out.println("**************************");
	}

}
