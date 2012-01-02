package org.powertac.visualizer.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.powertac.common.CustomerInfo;
import org.powertac.common.TariffTransaction;
import org.powertac.visualizer.Helper;

/**
 * Represents customer model that belongs to a particular broker.
 * 
 * @author Jurica Babic
 * 
 */
public class CustomerModel {

	Logger log = Logger.getLogger(CustomerModel.class);
	// active customers
	private int customerCount;
	// cash from/to particular broker:
	private double cashInflow;
	private double cashOutflow;
	// energy from/to particular broker;
	private double energyConsumption;
	private double energyProduction;
	// customer info
	private CustomerInfo customerInfo;
	// history
	private List<TariffTransaction> tariffTransactions;

	public CustomerModel(CustomerInfo customerInfo) {
		this.customerInfo = customerInfo;
		initialize();
	}

	public CustomerModel() {
		initialize();
	}

	private void initialize() {
		tariffTransactions = new ArrayList<TariffTransaction>();
	}

	/**
	 * Adds TarrifTransaction object to history and updates customer model.
	 * 
	 * @param tariffTransaction
	 */
	public void addTariffTransaction(TariffTransaction tariffTransaction) {
		log.info("\n CustomerModel: my tariffTrans: +\n" + tariffTransaction.toString());
		tariffTransactions.add(tariffTransaction);
		update(tariffTransaction);

	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CustomerModel) {
			CustomerModel customerModel = (CustomerModel) obj;
			if (customerModel.getCustomerInfo().getId() == getCustomerInfo().getId()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		// currently: hashcode is an int version of customerInfo ID.
		return (int) getCustomerInfo().getId();
	}

	private void update(TariffTransaction tariffTransaction) {

		// for all txtypes:
		updateCash(tariffTransaction.getCharge());
		updateEnergy(tariffTransaction.getKWh());

		// can't use switch with this enum, so if statements are used:

		// manage customerCount:
		customerCount += Helper.getCustomerCount(tariffTransaction);

	}

	private void updateEnergy(double kWh) {

		energyProduction += kWh;

		log.info("\n energy consumption:" + energyConsumption + " energy production:" + energyProduction);
	}

	private void updateCash(double charge) {

		cashOutflow += charge;
		log.info("\n CashInflow:" + cashInflow + " CashOutflow:" + cashOutflow);
	}

	public int getCustomerCount() {
		return customerCount;
	}

	public CustomerInfo getCustomerInfo() {
		return customerInfo;
	}

	public List<TariffTransaction> getTariffTransactions() {
		return tariffTransactions;
	}

	public double getCashInflow() {
		return cashInflow;
	}

	public double getCashOutflow() {
		return cashOutflow;
	}

	public synchronized double getCashBalance() {
		return cashInflow + cashOutflow;
	}

	public double getEnergyConsumption() {
		return energyConsumption;
	}

	public double getEnergyProduction() {
		return energyProduction;
	}

	public synchronized double getEnergyBalance() {
		return energyProduction + energyConsumption;
	}

	public synchronized double getCustomerShare() {
		if (customerInfo.getPopulation() != 0) {
			return (100.0)*customerCount / customerInfo.getPopulation();
		} else
			return 0;
	}

}
