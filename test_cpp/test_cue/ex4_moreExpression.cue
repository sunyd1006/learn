price: number
feel: *"good" | string
// Feel bad if price is too high
if price > 100 {
    feel: "bad"
}
price: 200





// if 示例
parameter: {
    name:   string
    image:  string
    useENV: bool
}
output: {
    spec: {
        containers: [{
            name:  parameter.name
            image: parameter.image
            if parameter.useENV == true {
                env: [{name: "my-env", value: "my-value"}]
            }
        }]
    }
}
parameter: {
    name:   "string"
    image:  "string"
    useENV: true
}

// for示例、类型遍历、切片遍历


import ("strings")

parameter: {
    outputs: [{ip: "1.1.1.1", hostname: "xxx.com"}, {ip: "2.2.2.2", hostname: "yyy.com"}]
}
output: {
    spec: {
        if len(parameter.outputs) > 0 {
            _x: [ for _, v in parameter.outputs {
                "\(v.ip) \(v.hostname)"
            }]
            message: "Visiting URL: " + strings.Join(_x, "")
        }
    }
}