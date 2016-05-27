/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ekstep.ecml.optimizr;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author feroz
 */
public class RecursiveProcessor implements Processor {
    
    private static final Logger logger = LogManager.getLogger();
    protected List<Processor> processors = new ArrayList<Processor>();
    protected Statistics stats = null;
    
    public RecursiveProcessor(Statistics stats) {
        this.stats = stats;
    }
    
    public void addProcessor(Processor proc) {
        this.processors.add(proc);
    }
    
    //@Override
    public File process(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    process(files[i]);
                    continue;
                }
                
                File file = files[i];
                FileType type = FileUtils.getFileType(file);
                long rawSize = file.length();
                long redSize = 0;
                
                for (Processor proc : processors) {    
                    if (proc.isApplicable(type)) {
                        try {
                            logger.debug("Processing file: " + file.getName());
                            File output = proc.process(file);
                            if (output != null) redSize = output.length();
                            stats.update(type, rawSize, redSize);
                        }
                        catch (Exception ex) {
                            logger.warn("Failed to apply processor: " + proc.getClass() + " on file: " + file.getName());
                        }
                    }
                }
            }
            return dir;
        }
        return null;
    }

    public boolean isApplicable(FileType type) {
        return (type == FileType.Directory);
    }
}
