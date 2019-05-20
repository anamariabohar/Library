package org.sda.librarymanagement.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.sda.librarymanagement.entity.Client;
import org.sda.librarymanagement.entity.dto.ClientDTO;
import org.sda.librarymanagement.repository.ClientRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class ClientService {

	@Autowired(required = true)
	private ClientRepository clientRepository;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private MembershipService membershipService;

	public List<Client> getAllClients() {
		return (List<Client>) clientRepository.findAll();
	}

	public Client getOneClientById(@PathVariable Long id) {
		return entityManager.find(Client.class, id);
	}

	public void saveClient(@RequestBody Client client) {
		clientRepository.save(client);
	}

	public Client updateClient(@PathVariable Long id, @RequestBody Client client) {
		Client existingClient = entityManager.find(Client.class, id);
		BeanUtils.copyProperties(client, existingClient);
		return clientRepository.save(existingClient);
	}

	public Client deleteClient(@PathVariable Long id) {
		Client existingClient = entityManager.find(Client.class, id);
		clientRepository.delete(existingClient);
		return existingClient;
	}

	public Client convertFromDTOToEntity(ClientDTO clientDTO) {
		Client client = new Client();

		client.setClientId(clientDTO.getClientId());
		client.setUsername(clientDTO.getUsername());
		client.setPassword(clientDTO.getPassword());
		client.setFirstName(clientDTO.getFirstName());
		client.setLastName(clientDTO.getLastName());
		client.setPhone(clientDTO.getPhone());
		client.setEmail(clientDTO.getEmail());
		client.setMembership(membershipService.getOneMembershipById(clientDTO.getMembership()));

		return client;
	}

	public boolean login(String username, String password) {
		Query query = entityManager
				.createNativeQuery("select * from clients where username:=username and password:=password");
		query.setParameter(0, username);
		query.setParameter(1, password);
		query.getSingleResult();

		if (query.getSingleResult() != null) {
			return true;
		}
		return false;
	}
}