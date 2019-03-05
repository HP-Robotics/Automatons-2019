
package frc.robot;

import java.util.function.IntSupplier;

public class Blueprint {
	double m_timeout;
	IntSupplier m_start;
	IntSupplier m_periodic;
	
	public Blueprint(double timeout, IntSupplier start, IntSupplier periodic) {
		m_timeout = timeout;
		m_start = start;
		m_periodic = periodic;
		
	}
}
