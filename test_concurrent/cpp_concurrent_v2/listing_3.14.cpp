#include <map>
#include <mutex>
#include <shared_mutex>
#include <string>
#include <iostream>

typedef std::string  dns_entry;

class dns_cache {
    std::map<std::string, dns_entry> entries;
    std::recursive_mutex re_mutex;
    std::mutex mutex;

public:
    dns_entry find_entry_with_mutex(std::string const& domain) {
        std::lock_guard<std::mutex> lk_once(mutex);
        std::map<std::string, dns_entry>::const_iterator const it = entries.find(domain);

        // lock twice
        std::lock_guard<std::mutex> lk_twice(mutex);
        return (it == entries.end()) ? dns_entry() : it->second;
    }

    dns_entry find_entry_with_remutex(std::string const& domain) {
        std::lock_guard<std::recursive_mutex> lk_once(re_mutex);
        std::map<std::string, dns_entry>::const_iterator const it = entries.find(domain);

        // lock twice
        std::lock_guard<std::recursive_mutex> lk_twice(re_mutex);
        return (it == entries.end()) ? dns_entry() : it->second;
    }
    void update_or_add_entry(std::string const& domain, dns_entry const& dns_details) {
        std::lock_guard<std::recursive_mutex> lk(re_mutex);
        entries[domain] = dns_details;
    }
};

int main() {
    dns_cache cache;
    cache.update_or_add_entry("www.boost.org", dns_entry("www.boost.org"));
    std::cout << "find result: " << cache.find_entry_with_remutex("www.boost.org");
    std::cout << "find result: " << cache.find_entry_with_mutex("www.boost.org");
    return 0;
}
