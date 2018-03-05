package com.lightbend.connectedcar.impl;

import akka.Done;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;
import com.lightbend.connectedcar.api.CCarSummary;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.ReadSide;
import com.lightbend.lagom.javadsl.persistence.ReadSideProcessor;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraReadSide;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;
import com.lightbend.utils.CompletionStageUtils;
import org.pcollections.PCollection;
import org.pcollections.PSequence;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static com.lightbend.lagom.javadsl.persistence.cassandra.CassandraReadSide.completedStatement;

@Singleton
public class CCarStreamRepository {

    private final CassandraSession session;

    @Inject
    public CCarStreamRepository(CassandraSession session, ReadSide readSide) {
        this.session = session;

        readSide.register(CarSummaryEventProcessor.class);
    }

    public CompletionStage<Collection<CCarSummary>> summaries() {
        return session.selectAll(
                    "SELECT * from carSummaries"
                )
                .thenApply(List::stream)
                .thenApply(rows -> rows.map(this::toSummary))
                .thenApply(summaries -> summaries.collect(Collectors.toSet()));
    }

    public CompletionStage<CCarSummary> summary(String carId) {

        return session.<CCarSummary>selectOne("SELECT * from carSummaries WHERE carId = ?", carId)
                .thenApply(maybeRow -> {
                    if (maybeRow.isPresent()) {
                        return toSummary(maybeRow.get());
                    }

                    return CCarSummary.empty();
                });
    }

    private CCarSummary toSummary(Row row) {
        return new CCarSummary(row.getString("carId"), row.getInt("powerConsumption"), row.getInt("speed"), row.getInt("motorTemp"),  row.getString("driver"), row.getDecimal("latitude"), row.getDecimal("longitude"), row.getString("status"), row.getInt("batteryLevel"));
    }

    private static class CarSummaryEventProcessor extends ReadSideProcessor<CCarEvent> {

        private final CassandraSession session;
        private final CassandraReadSide readSide;

        private PreparedStatement insertSummaryStatement;
        private PreparedStatement deleteSummaryStatement;

        @Inject
        public CarSummaryEventProcessor(CassandraSession session, CassandraReadSide readSide) {
            this.session = session;
            this.readSide = readSide;
        }

        @Override
        public ReadSideProcessor.ReadSideHandler<CCarEvent> buildHandler() {
            return readSide.<CCarEvent>builder("cCarOffset")
                    .setGlobalPrepare(this::createTable)
                    .setPrepare(tag -> prepareStatements())
                    .setEventHandler(CCarEvent.TelemetryChanged.class, e -> insertSummary(e.getCarId(), e.getPowerConsumption(), e.getSpeed(), e.getMotorTemp(), e.getDriver(), e.getLatitude(), e.getLongitude(), e.getStatus(), e.getBatteryLevel()))
                    .build();
        }

        @Override
        public PSequence<AggregateEventTag<CCarEvent>> aggregateTags() {
            return CCarEvent.TAG.allTags();
        }

        private CompletionStage<Done> createTable() {
            return session.executeCreateTable(
                    "CREATE TABLE IF NOT EXISTS carSummaries (" +
                            "carId text," +
                            "powerConsumption int," +
                            "speed int," +
                            "motorTemp int," +
                            "driver text," +
                            "latitude decimal," +
                            "longitude decimal," +
                            "status text," +
                            "batteryLevel int," +
                            "PRIMARY KEY (carId)" +
                            ")"
            );
        }

        private CompletionStage<Done> prepareStatements() {

            return CompletionStageUtils.doAll(

                    session.prepare(
                            "INSERT INTO carSummaries(" +
                                    "carId," +
                                    "powerConsumption," +
                                    "speed," +
                                    "motorTemp," +
                                    "driver," +
                                    "latitude," +
                                    "longitude," +
                                    "status," +
                                    "batteryLevel" +
                                    ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
                    )
                            .thenAccept(statement -> insertSummaryStatement = statement),

                    session.prepare(
                            "DELETE FROM carSummaries" +
                                    " WHERE carId = ?"
                    )
                            .thenAccept(statement -> deleteSummaryStatement = statement)
            );
        }

        private CompletionStage<List<BoundStatement>> insertSummary(String carId, int powerConsumption, int speed, int motorTemp, String driver, BigDecimal latitude, BigDecimal longitude, String status, int batteryLevel) {
            return completedStatement(insertSummaryStatement.bind(carId, powerConsumption, speed, motorTemp, driver, latitude, longitude, status, batteryLevel));
        }

        private CompletionStage<List<BoundStatement>> deleteSummary(String carId) {
            return completedStatement(deleteSummaryStatement.bind(carId));
        }
    }
}
