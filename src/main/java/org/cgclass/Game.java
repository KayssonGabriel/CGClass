package org.cgclass;

import org.cgclass.context.GraphicContext;
import org.cgclass.context.Platform;
import org.cgclass.event.OnWindowResizeEvent;
import org.cgclass.event.RecompileShaderEvent;
import org.cgclass.shader.Shader;
import org.cgclass.shader.ShaderCompiler;
import org.cgclass.shader.ShaderProgram;
import org.gear.framework.application.Application;
import org.gear.framework.core.service.event.reactive.Reactive;
import org.gear.framework.core.service.input.Input;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL45.*;

public class Game extends Application {

    final float squareSize = 0.5f;

    boolean isSpacePressed = false;

    // Triângulo 1 (à esquerda)
    float[] vertices = {
            -squareSize - 1.5f, -squareSize, 0.0f,
            squareSize - 1.5f, -squareSize, 0.0f,
            squareSize - 1.5f, squareSize, 0.0f,
    };

    // Triângulo 2 (à direita)
    float[] vertices2 = {
            -squareSize + 1.5f, squareSize, 0.0f,
            squareSize + 1.5f, squareSize, 0.0f,
            -squareSize + 1.5f, -squareSize, 0.0f,
    };

    // Quadrado (ao centro)
    float[] squareVertices = {
            -squareSize, -squareSize, 0.0f,
            squareSize, -squareSize, 0.0f,
            squareSize, squareSize, 0.0f,
            -squareSize, squareSize, 0.0f
    };


    GraphicContext context = new GraphicContext(800, 800, "Hello OpenGL", Platform.Linux);
    int VAO1;
    int VBO1;
    int VAO2;
    int VBO2;
    int VAO3;
    int VBO3;

    Matrix4f transformationMatrix = new Matrix4f();

    Shader shader;
    ShaderCompiler compiler;
    ShaderProgram program;

    Random random = new Random();
    Vector4f color = new Vector4f(1.0f, 0.0f, 0.0f, 1.0f);

    @Override
    public void start() {

        shader = new Shader(
                "src/main/resources/shader.vert",
                "src/main/resources/shader.frag");

        transformationMatrix.identity();
        transformationMatrix.rotate((float) Math.toDegrees(60),
                new Vector3f(0f, 0f, 1f));

        transformationMatrix.scale(0.5f);

        compiler = new ShaderCompiler();
        program = compiler.compile(shader);

        // Configuração do primeiro triângulo -------------------------------------
        VAO1 = glGenVertexArrays();          // Cria um VAO e retorna seu ID
        glBindVertexArray(VAO1);             // Vincula o VAO para escrita ou leitura

        VBO1 = glGenBuffers();               // Cria um buffer e retorna seu ID
        glBindBuffer(GL_ARRAY_BUFFER, VBO1); // Vincula o buffer ao VAO vinculado atualmente, que é o que criamos

        // Define os dados do buffer atualmente vinculado, passamos o tipo do buffer, os dados,
        // e o tipo de escrita, passamos GL_STATIC_DRAW, significa que escreveremos os dados de
        // forma estática, ou seja, eles são imutáveis, ou estáticos.
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        // Agora descrevemos como estes dados devem se comportar, dizemos que eles estarão na linha zero
        // do VAO, dizemos que eles representam um vetor de 3 componentes, com o tipo Float, não normalizados,
        // e passamos o deslocamento entre cada valor (3 * Float.BYTES), o último parâmetro pode ser ignorado.
        glVertexAttribPointer(0, 3, GL_FLOAT, false,
                3 * Float.BYTES, 0);

        // Em seguida habilitamos a linha do VAO a qual acabamos de descrever o comportamento dos dados.
        glEnableVertexAttribArray(0);

        // Por ultimo desvinculamos o VAO.
        glBindVertexArray(0);


        // Configuração do segundo triângulo -------------------------------------
        VAO2 = glGenVertexArrays();
        glBindVertexArray(VAO2);

        VBO2 = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VBO2);
        glBufferData(GL_ARRAY_BUFFER, vertices2, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glBindVertexArray(0);

        // Configuração do quadrado -------------------------------------
        VAO3 = glGenVertexArrays();
        glBindVertexArray(VAO3);

        VBO3 = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VBO3);
        glBufferData(GL_ARRAY_BUFFER, squareVertices, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glBindVertexArray(0);


    }

    @Override
    public void update() {

        // Limpamos o framebuffer de escrita
        context.clear();

        program.bind();
        program.setMat4("uTransformationMatrix",
                transformationMatrix);
        program.setVec4("Color", color);

        // Vinculamos o VAO para usar seus dados
        glBindVertexArray(VAO1);

        // Desenhamos na tela usando os dados deste VAO, dizemos para usar TRIÂNGULOS como primitiva
        // gráfica, dizemos para começar na posição da nossa lista de dados, e dizemos para desenhar
        // usando 6 dados de cada linha da tabela.
        glDrawArrays(GL_TRIANGLES, 0, 3);

        // Desvinculamos o VAO.
        glBindVertexArray(0);


        glBindVertexArray(VAO2);
        glDrawArrays(GL_TRIANGLES, 0, 3);
        glBindVertexArray(0);

        glBindVertexArray(VAO3);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
        glBindVertexArray(0);


        // Trocamos o buffer que desenhamos de lugar com o buffer de saída da tela
        context.swapBuffers();

        // Capturamos os eventos da janela
        context.pollEvents();


        if (context.isCloseRequested()) {
            shutdown();
        }

        // Rotacionar no sentido anti-horário
        if (Input.isKeyPressed(GLFW_KEY_A)) {
            transformationMatrix.rotate((float) Math.toRadians(-1), 0f, 0f, 1f);
        }

        // Rotacionar no sentido horário
        if (Input.isKeyPressed(GLFW_KEY_D)) {
            transformationMatrix.rotate((float) Math.toRadians(1), 0f, 0f, 1f);
        }

        // Definir cor aleatória
        if (Input.isKeyPressed(GLFW_KEY_W)) {
            color.set(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1.0f);
        }

        if (Input.isKeyPressed(GLFW_KEY_SPACE) && !isSpacePressed) {
            Application.dispatchEvent(new RecompileShaderEvent());
            isSpacePressed = true;
        }

        if (!Input.isKeyPressed(GLFW_KEY_SPACE)) {
            isSpacePressed = false;
        }
    }

    @Reactive
    public void onWindowResize(OnWindowResizeEvent event) {

    }

    @Reactive
    public void onRecompileShaderRequest(RecompileShaderEvent event) {
        System.out.println("Recompiling shaders.");
    }
}