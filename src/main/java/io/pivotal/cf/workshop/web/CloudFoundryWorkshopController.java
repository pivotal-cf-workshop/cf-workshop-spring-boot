package io.pivotal.cf.workshop.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import io.pivotal.cf.workshop.entity.Attendee;
import io.pivotal.cf.workshop.repository.AttendeeRepository;

@Controller
public class CloudFoundryWorkshopController {

	private static final Logger logger = LoggerFactory.getLogger(CloudFoundryWorkshopController.class);

	@Autowired(required = false)
	private CloudFactory cloudFactory;
	
	@Autowired
	private AttendeeRepository attendeeRepo;

	@RequestMapping("/")
	public String index(Model model) throws Exception {
		for (String key : System.getenv().keySet()) {
			System.out.println(key + ":" + System.getenv(key));
		}

		addAppInstanceIndex(model);

		return "index";
	}

	private void addAppInstanceIndex(Model model) throws Exception {

		String instanceIndex = System.getenv("CF_INSTANCE_INDEX");

		if (instanceIndex == null) {
			logger.info("No CF_INSTANCE_INDEX, going to VCAP_APPLICATION");
			if (getVCAPMap() != null)
				instanceIndex = Integer.toString((Integer) getVCAPMap().get("instance_index"));
		}

		model.addAttribute("instanceIndex", instanceIndex != null ? instanceIndex : "no index environment variable");
	}

	@SuppressWarnings("rawtypes")
	private Map getVCAPMap() throws Exception {
		String vcapApplication = System.getenv("VCAP_APPLICATION");
		ObjectMapper mapper = new ObjectMapper();
		if (vcapApplication != null) {
			Map vcapMap = mapper.readValue(vcapApplication, Map.class);
			return vcapMap;
		}

		return null;

	}

	/**
	 * Gets basic environment information.
	 * 
	 * @param model
	 *            The model for this action.
	 * @return The path to the view.
	 * @throws Exception
	 */
	@RequestMapping(value = "/env", method = RequestMethod.GET)
	public String env(Model model) throws Exception {

		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy h:mm a");
		String serverTime = dateFormat.format(date);
		model.addAttribute("serverTime", serverTime);

		String port = System.getenv("PORT");
		model.addAttribute("port", port);

		@SuppressWarnings("rawtypes")
		Map vcapMap = getVCAPMap();

		if (vcapMap != null) {
			model.addAttribute("vcapApplication", vcapMap);
		}

		String vcapServices = System.getenv("VCAP_SERVICES");
		model.addAttribute("vcapServices", vcapServices);

		logger.info("Current date and time = [{}], port = [{}].", serverTime, port);

		if (cloudFactory != null) {

			Cloud cloud = cloudFactory.getCloud();

			if (cloud != null) {
				List<ServiceInfo> serviceInfos = cloud.getServiceInfos();

				if (serviceInfos != null && serviceInfos.size() > 0) {
					ArrayList<String> services = new ArrayList<String>(serviceInfos.size());

					for (ServiceInfo si : serviceInfos) {

						String sName = si.getClass().getSimpleName();

						if (sName.indexOf("ServiceInfo") != -1) {
							services.add(si.getId() + " - " + sName.substring(0, sName.indexOf("ServiceInfo")));
						}

						logger.info("Service:" + si.getId() + "-" + sName);
					}
					model.addAttribute("serviceInfos", services);
				} else {
					logger.info("No Bound Services");

				}

			}

		} else {
			logger.info("no cloudFactory");
		}

		return "env";
	}

	/**
	 * Action to initiate shutdown of the system. In CF, the application
	 * <em>should</em> restart. In other environments, the application runtime
	 * will be shut down.
	 */
	@RequestMapping(value = "/kill", method = RequestMethod.GET)
	public void kill() {

		logger.warn("*** The system is shutting down. ***");
		System.exit(0);

	}

	@RequestMapping(value = "/addAttendee", method = RequestMethod.GET)
	public String addAttendee() {
		return "addAttendee";
	}

	@RequestMapping(value = "/addAttendee", method = RequestMethod.POST)
	public String addAttendee(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
			@RequestParam("emailAddress") String emailAddress, Model model) throws Exception {

		Attendee attendee = new Attendee();
		attendee.setFirstName(firstName);
		attendee.setLastName(lastName);
		attendee.setEmailAddress(emailAddress);

		attendeeRepo.save(attendee);

		return this.attendees(model);
	}
	
	/**
	 * Action to get a list of all attendees.
	 * 
	 * @param model
	 *            The model for this action.
	 * @return The path to the view.
	 */
	@RequestMapping(value = "/attendees", method = RequestMethod.GET)
	public String attendees(Model model) throws Exception{

		Iterable<Attendee> attendees = attendeeRepo.findAll();

		model.addAttribute("attendees", attendees);
		addAppInstanceIndex(model);
		return "attendees";
	}

}