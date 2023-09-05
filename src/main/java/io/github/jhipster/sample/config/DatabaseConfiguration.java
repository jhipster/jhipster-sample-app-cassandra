package io.github.jhipster.sample.config;

import com.datastax.oss.driver.api.core.data.TupleValue;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.core.type.TupleType;
import jakarta.annotation.Nonnull;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.cassandra.core.convert.CassandraCustomConversions;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

@Configuration
public class DatabaseConfiguration {

    @Bean
    public CassandraCustomConversions cassandraCustomConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new TupleToZonedDateTimeConverter());
        converters.add(new ZonedDateTimeToTupleConverter());
        return new CassandraCustomConversions(converters);
    }

    @ReadingConverter
    class TupleToZonedDateTimeConverter implements Converter<TupleValue, ZonedDateTime> {

        @Override
        public ZonedDateTime convert(TupleValue source) {
            Instant instant = source.getInstant(0);
            ZoneId zoneId = ZoneId.of(source.getString(1));
            return instant.atZone(zoneId);
        }
    }

    @WritingConverter
    class ZonedDateTimeToTupleConverter implements Converter<ZonedDateTime, TupleValue> {

        private TupleType type = DataTypes.tupleOf(DataTypes.TIMESTAMP, DataTypes.TEXT);

        @Override
        public TupleValue convert(@Nonnull ZonedDateTime source) {
            TupleValue tupleValue = type.newValue();
            tupleValue.setInstant(0, source.toInstant());
            tupleValue.setString(1, source.getZone().toString());
            return tupleValue;
        }
    }
}
