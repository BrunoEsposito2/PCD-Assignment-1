package pcd.lab04.gui.chrono2_strict;

/**
 * 
 * A version strictly following the discipline 
 * about active (agent) & passive (monitor) components.
 * 
 * @author aricci
 *
 */
public class TestCounting {
	public static void main(String[] args) {
		Counter counter = new Counter(0);
		Controller controller = new Controller(counter);
        CounterView view = new CounterView(controller, counter.getValue());
        controller.setView(view);
        view.display();
	}
}
