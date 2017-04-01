package fxClasses;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.io.InputStream;

public class SpringFXMLLoader {

    private static final ApplicationContext APPLICATION_CONTEXT = new ClassPathXmlApplicationContext("ApplicationContext.xml");

    public static Parent load(String url) {
        InputStream fxmlStream = null;
        try {
            fxmlStream = SpringFXMLLoader.class.getResourceAsStream(url);
            FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(aClass -> APPLICATION_CONTEXT.getBean(aClass));
            loader.load(fxmlStream);
            return loader.getRoot();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (fxmlStream != null) {
                try {
                    fxmlStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void setControllerFactoryToLoader(FXMLLoader loader) {
        loader.setControllerFactory(aClass -> APPLICATION_CONTEXT.getBean(aClass));
    }

    public static CustomRecordElement getCustomRecordElement(){
        return (CustomRecordElement) APPLICATION_CONTEXT.getBean("CustomRecordElement");
    }
}
