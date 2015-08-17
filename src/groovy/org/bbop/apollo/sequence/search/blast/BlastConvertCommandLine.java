package org.bbop.apollo.sequence.search.blast;

import org.bbop.apollo.sequence.search.AlignmentParsingException;
import org.bbop.apollo.sequence.search.SequenceSearchTool;
import org.bbop.apollo.sequence.search.SequenceSearchToolException;
import org.bbop.apollo.sequence.search.blast.BlastAlignment;
import org.bbop.apollo.sequence.search.blast.TabDelimittedAlignment;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.codehaus.groovy.grails.web.json.JSONObject;

public class BlastConvertCommandLine extends SequenceSearchTool {

    private String blastBin;
    private String database;
    private String blastUserOptions;
    private String tmpDir;
    private boolean removeTmpDir;

    @Override
    public void parseConfiguration(JSONObject config) throws SequenceSearchToolException {
        try {
            if(config.has("search_exe")) { blastBin = config.getString("search_exe"); }
            else { throw new SequenceSearchToolException("No blast exe specified"); }
            if(config.has("database")&&config.getString("database")!="") {database = config.getString("database"); }
            else { throw new SequenceSearchToolException("No database configured"); }
            if(config.has("params")) {blastUserOptions = config.getString("params");}
            else { /* no extra params needed */ }
            if(config.has("removeTmpDir")) {removeTmpDir=config.getBoolean("removeTmpDir"); }
            else { removeTmpDir=true; }
            if(config.has("tmp_dir")) {tmpDir=config.getString("tmp_dir"); }
        } catch (Exception e) {
            throw new SequenceSearchToolException("Error parsing configuration: " + e.getMessage(), e);
        }
    }


    @Override
    public Collection<BlastAlignment> search(String uniqueToken, String query, String databaseId) throws SequenceSearchToolException {
        File dir = null;
        Path p = null;
        try {
            if(tmpDir==null) {
                p = Files.createTempDirectory("blast_tmp");
            }
            else {
                p = Files.createTempDirectory(new File(tmpDir).toPath(),"blast_tmp");
            }
            dir = p.toFile();

            return runSearch(dir, query, databaseId);
        }
        catch (IOException e) {
            throw new SequenceSearchToolException("Error running search: " + e.getMessage(), e);
        }
        catch (AlignmentParsingException e) {
            throw new SequenceSearchToolException("Alignment parsing error: " + e.getMessage(), e);
        }
        catch (InterruptedException e) {
            throw new SequenceSearchToolException("Error running search: " + e.getMessage(), e);
        }
        finally {
            if (removeTmpDir && dir!=null) {
                deleteTmpDir(dir);
            }
        }
    }
    
    private Collection<BlastAlignment> runSearch(File dir, String query, String databaseId)
            throws IOException, AlignmentParsingException, InterruptedException {
        PrintWriter log = new PrintWriter(new BufferedWriter(new FileWriter(dir + "/search.log")));
        String queryArg = createQueryFasta(dir, query);
        String databaseArg = database + (databaseId != null ? ":" + databaseId : "");
        String outputArg = dir.getAbsolutePath() + "/results.tab";
        String outputXml = dir.getAbsolutePath() + "/results.xml";
        String outputGff = dir.getAbsolutePath() + "/results.gff";
        List<String> commands = new ArrayList<String>();
        commands.add(blastBin);
        commands.add("-db");
        commands.add(databaseArg);
        commands.add("-query");
        commands.add(queryArg);
        commands.add("-out");
        commands.add(outputXml);
        if (blastUserOptions != null && blastUserOptions.length() > 0) {
            for (String option : blastUserOptions.split("\\s+")) {
                commands.add(option);
            }
        }

        ProcessBuilder pb = new ProcessBuilder(commands);
        Process p = pb.start();
        p.waitFor();
        String line;
        BufferedReader stdout = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while ((line = stdout.readLine()) != null) {
            log.println(line);
        }
        log.println();
        BufferedReader stderr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        while ((line = stderr.readLine()) != null) {
            log.println(line);
        }
        p.destroy();


        commands.clear();
        commands.add(blastBin);
        commands.add("-db");
        commands.add(databaseArg);
        commands.add("-query");
        commands.add(queryArg);
        commands.add("-out");
        commands.add(outputArg);
        commands.add("-outfmt");
        commands.add("6");
        if (blastUserOptions != null && blastUserOptions.length() > 0) {
            for (String option : blastUserOptions.split("\\s+")) {
                commands.add(option);
            }
        }


        pb = new ProcessBuilder(commands);
        p = pb.start();
        p.waitFor();
        stdout = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while ((line = stdout.readLine()) != null) {
            log.println(line);
        }
        log.println();
        stderr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        while ((line = stderr.readLine()) != null) {
            log.println(line);
        }
        log.close();
        p.destroy();
        Collection<BlastAlignment> matches = new ArrayList<BlastAlignment>();
        BufferedReader in = new BufferedReader(new FileReader(outputArg));
        while ((line = in.readLine()) != null) {
            matches.add(new TabDelimittedAlignment(line));
        }
        in.close();



        commands.clear();

        commands.add("bp_search2gff.pl");
        commands.add("--input");
        commands.add(outputXml);
        commands.add("--method");
        commands.add("blastn");
        commands.add("--addid");
        commands.add("--version");
        commands.add("3");
        commands.add("--type");
        commands.add("hit");
        commands.add("--output");
        commands.add(outputGff);



        pb = new ProcessBuilder(commands);
        p = pb.start();
        p.waitFor();
        stdout = new BufferedReader(new InputStreamReader(p.getInputStream()));
        log.println("stdout:");
        while ((line = stdout.readLine()) != null) {
            log.println(line);
        }
        log.println();
        stderr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        log.println("stderr:");
        while ((line = stderr.readLine()) != null) {
            log.println(line);
        }
        log.close();
        p.destroy();

        return matches;
    }

    private void deleteTmpDir(File dir) {
        if (!dir.exists()) {
            return;
        }
        for (File f : dir.listFiles()) {
            f.delete();
        }
        dir.delete();
    }


    private String createQueryFasta(File dir, String query) throws IOException {
        String queryFileName = dir.getAbsolutePath() + "/query.fa";
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(queryFileName)));
        out.println(">query");
        out.println(query);
        out.close();
        return queryFileName;
    }
    
}
