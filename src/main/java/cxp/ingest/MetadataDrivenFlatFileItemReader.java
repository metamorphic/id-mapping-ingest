package cxp.ingest;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by markmo on 7/04/15.
 */
public class MetadataDrivenFlatFileItemReader extends FlatFileItemReader<Map<String, Object>> {

    MetadataProvider metadataProvider;

    @Override
    public void setResource(Resource resource) {
        super.setResource(resource);
        metadataProvider.setFilename(resource.getFilename());
        final FileDataset fileDataset = metadataProvider.getFileDataset();
        if (fileDataset.isHeaderRow()) {
            setLinesToSkip(1);
        }
        final char quotechar;
        String textQualifier = fileDataset.getTextQualifier();
        if (textQualifier == null) {
            quotechar = ',';
        } else {
            quotechar = textQualifier.charAt(0);
        }
        Assert.notNull(fileDataset.getColumnNames());
        setLineMapper(new DefaultLineMapper<Map<String, Object>>() {{
            setLineTokenizer(new DelimitedLineTokenizer(fileDataset.getColumnDelimiter()) {{
                setNames(fileDataset.getColumnNames());
                setQuoteCharacter(quotechar);
            }});
            setFieldSetMapper(new FieldSetMapper<Map<String, Object>>() {
                @Override
                public Map<String, Object> mapFieldSet(FieldSet fieldSet) throws BindException {
                    Map<String, Object> fields = new HashMap<String, Object>();
                    if (fileDataset.getColumns() != null) {
                        for (FileColumn column : fileDataset.getColumns()) {
                            if ("integer".equals(column.getValueTypeName())) {
                                fields.put(column.getName(), fieldSet.readInt(column.getColumnIndex() - 1));
                            } else {
                                fields.put(column.getName(), fieldSet.readString(column.getColumnIndex() - 1));
                            }
                        }
                    }
                    return fields;
                }
            });
        }});
    }

    public void setMetadataProvider(MetadataProvider metadataProvider) {
        this.metadataProvider = metadataProvider;
    }
}
