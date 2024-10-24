package org.cgclass.shader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Shader {

    private String vertexSource;
    private String fragmentSource;

    private final String vertexPath;
    private final String fragmentPath;

    public Shader(String vertexPath, String fragmentPath) {
        vertexSource = getFileContent(vertexPath);
        fragmentSource = getFileContent(fragmentPath);
        this.vertexPath = vertexPath;
        this.fragmentPath = fragmentPath;
    }

    public String getVertexSource() {
        return vertexSource;
    }

    public String getFragmentSource() {
        return fragmentSource;
    }

    private String getFileContent(String fileName) {
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return content.toString();
    }

    public void reset() {
        vertexSource = getFileContent(vertexPath);
        fragmentSource = getFileContent(fragmentPath);
    }
}
